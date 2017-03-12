package com.android.morephone.domain;

/**
 * Created by AnPEthan on 9/6/2016.
 */

public abstract class UseCase<Q extends UseCase.RequestValue, P extends UseCase.ResponseValue> {

    private Q mRequestValue;

    private UseCaseCallback<P> mUseCaseCallback;

    public void setRequestValue(Q requestValue){
        mRequestValue = requestValue;
    }

    public Q getRequestValue(){
        return mRequestValue;
    }

    public void setUseCaseCallback(UseCaseCallback<P> callback){
        mUseCaseCallback = callback;
    }

    public UseCaseCallback<P> getUseCaseCallback(){
        return mUseCaseCallback;
    }

    void run(){
        executeUseCase(mRequestValue);
    }

    protected abstract void executeUseCase(Q requestValue);

    /**
     * Data passed to a request.
     */
    public interface RequestValue{}

    /**
     * Data received from a request.
     */
    public interface ResponseValue{}

    public interface UseCaseCallback<R>{
        void onSuccess(R response);
        void onError();
    }
}
