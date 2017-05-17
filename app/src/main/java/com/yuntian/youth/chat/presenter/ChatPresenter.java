package com.yuntian.youth.chat.presenter;

import android.util.Log;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.yuntian.youth.chat.model.ChatItem;
import com.yuntian.youth.chat.service.ChatSevice;
import com.yuntian.youth.chat.view.callback.ChatView;
import com.yuntian.youth.global.Constant;
import com.yuntian.youth.register.model.bean.User;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


/**
 * Created by huxianguang on 2017/5/14.
 */

public class ChatPresenter extends MvpBasePresenter<ChatView>{
    //查询聊天记录的条数
    public static final int DEFAULT_PAGE_SIZE = 20;

    private boolean hasMoreDate=true;//判断是否可以获取聊天记录

    public ChatPresenter(){
    }

    /**
     * 发送消息
     * @param message
     * @param user
     */
    public void sendMessage(final String message, final User user){

                ChatSevice.sendMessage(message,user.getObjectId());

        getView().MessageSuccess(message);
    }

    /**
     * 获取此会话中所有的未读消息
     * @param chatID
     */
    public void loadConversationAllMessage(final String chatID, final User user){
        Observable.create(new Observable.OnSubscribe<List<EMMessage>>() {
            @Override
            public void call(Subscriber<? super List<EMMessage>> subscriber) {
                //获取此会话的所有消息
                EMConversation conversation=EMClient.getInstance().chatManager().getConversation(chatID);
                if (conversation!=null){
                    List<EMMessage> messageList=conversation.getAllMessages();
                    //将此会话的未读数清零
                    conversation.markAllMessagesAsRead();
                    subscriber.onNext(messageList);
                }
            }
        })
          .flatMap(new Func1<List<EMMessage>, Observable<List<ChatItem>>>() {
              @Override
              public Observable<List<ChatItem>> call(List<EMMessage> emMessageList) {
                  List<ChatItem> chatItemList=EMMessageToChatItem(emMessageList,user);
                  return Observable.just(chatItemList);
              }
          })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<List<ChatItem>>() {
                @Override
                public void call(List<ChatItem> chatItems) {
                    getView().MessageReceived(chatItems);
                }
            }, new Action1<Throwable>() {
                @Override
                public void call(Throwable throwable) {
                    Log.v("=====erro",throwable.getMessage());
                }
            });
    }

    /**
     * 当有消息时  执行跟新到cycleview
     * @param messages
     */
    public void getMessage(final List<EMMessage> messages){
        Observable.create(new Observable.OnSubscribe<List<ChatItem>>() {
            @Override
            public void call(Subscriber<? super List<ChatItem>> subscriber) {
                List<ChatItem> chatItemList=new ArrayList<>();
                for (EMMessage emMessage:messages){
                    ChatItem chatItem =new ChatItem();
                    chatItem.setEMMessage(emMessage);
                    chatItem.setType(Constant.CHAT_MESSAGE_TYPE_FRIENDS);
                    chatItem.setUser(User.getCurrentUser());
                    chatItemList.add(chatItem);
                }
                subscriber.onNext(chatItemList);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<ChatItem>>() {
                    @Override
                    public void call(List<ChatItem> chatItems) {
                        getView().MessageReceived(chatItems);

                    }
                });
    }

    /**
     * 获取聊天记录
     * @param chatID
     * @param firstMessage 当前显示的第一条数据
     */
    public void loadMoreMessages(final User user, final EMMessage firstMessage){
        if (hasMoreDate){
            Observable.create(new Observable.OnSubscribe<List<EMMessage>>() {
                @Override
                public void call(Subscriber<? super List<EMMessage>> subscriber) {
                    EMConversation conversation = EMClient.getInstance().chatManager().getConversation(user.getObjectId());
                    List<EMMessage> messages=conversation.loadMoreMsgFromDB(firstMessage.getMsgId(),DEFAULT_PAGE_SIZE);
                    hasMoreDate=(messages.size()==DEFAULT_PAGE_SIZE);
                    subscriber.onNext(messages);
                }
            })
            .flatMap(new Func1<List<EMMessage>, Observable<List<ChatItem>>>() {
                @Override
                public Observable<List<ChatItem>> call(List<EMMessage> emMessageList) {
                    List<ChatItem> chatItem=EMMessageToChatItem(emMessageList,user);
                    return Observable.just(chatItem);
                }
            })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<List<ChatItem>>() {
                @Override
                public void call(List<ChatItem> chatItems) {
                    getView().LoadMoreSuccess(chatItems);
                }
            }, new Action1<Throwable>() {
                @Override
                public void call(Throwable throwable) {
                    Log.v("====erro",throwable.getMessage());
                }
            });
        }
    }

    /**
     * 根据EMMessage转换成chatitem
     * @param emMessageList
     * @param user
     * @return
     */
    public List<ChatItem> EMMessageToChatItem(List<EMMessage> emMessageList,User user){
        List<ChatItem> chatItemList=new ArrayList<>();
        for (EMMessage emMessage:emMessageList){
            ChatItem chatItem=new ChatItem();
            if (emMessage.getUserName().equals(user.getObjectId())){
                //不是自己发送的信息
                chatItem.setUser(user);
                chatItem.setType(Constant.CHAT_MESSAGE_TYPE_FRIENDS);
            }else {
                chatItem.setUser(User.getCurrentUser());
                chatItem.setType(Constant.CHAT_MESSAGE_TYPE_MY);
            }
            chatItem.setEMMessage(emMessage);
            chatItemList.add(chatItem);
        }
        return chatItemList;
    }

}
