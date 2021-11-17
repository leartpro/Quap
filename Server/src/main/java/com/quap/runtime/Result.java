package com.quap.runtime;

public class Result implements FutureCallback<Integer>{
        @Override
        public void onSuccess(Integer result) {
            System.out.println("Result: " + result);
        }

        @Override
        public void onFailure(Throwable failure) {
            failure.printStackTrace();
        }
}
