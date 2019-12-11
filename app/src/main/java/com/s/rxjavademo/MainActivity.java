package com.s.rxjavademo;

import androidx.appcompat.app.AppCompatActivity;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.INotificationSideChannel;
import android.view.View;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final List<String> alphabets = Arrays.asList("A","B","C","D","E");

        ObservableCreate(alphabets);

        ObservableFrom();
        ObservableInterval();
        ObservableJust();

        /*
        Difference between Observable.from() and Observable.just() — For the same input, if you see the above code,
        Observable.just() emits only once whereas Observable.from()emits n times i.e. the length of the array,
        in this case 6
        */

        ObservableRange();

        ObservableRepeat();
        
        ObservableTimer();

        ObservableBuffer();

        getOriginalObservable()
                .map(new Function<Integer, Integer>() {
                    @Override
                    public Integer apply(Integer integer) throws Exception {
                        if(integer%5 ==0){
                            return integer;
                        }else{
                            return 0;
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        if(integer!=0)
                        System.out.println("ObservableMap onNext: " + integer);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

        try {
            ObservableFlatMap();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private void ObservableFlatMap() throws InterruptedException {

        getOriginalObservable()
                .flatMap(new Function<Integer, Observable<Integer>>() {
                    @Override
                    public Observable<Integer> apply(final Integer integer)  {
                        return getModifiedObservable(integer);
                    }
                })
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        System.out.println("ObservableFlatMap onNext: " + integer);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

        Thread.sleep(2000);
    }

    private Observable<Integer> getModifiedObservable(final Integer integer) {
        return Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws InterruptedException {
                emitter.onNext((integer * 5));
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io());
    }

    private Observable<Integer> getOriginalObservable() {

        final List<Integer> list = Arrays.asList(1,20,3,4,5,60,7,8,90);

        return Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                    for(Integer i : list){
                        if(!emitter.isDisposed()){
                            emitter.onNext(i);
                        }
                    }

                    if(!emitter.isDisposed()){
                        emitter.isDisposed();
                    }
            }
        });
    }

    private void ObservableBuffer() {

        /*
        * The below sample transforms an Observable using Observable.buffer() method.
        *  The below code will emit 2 items at a time since the buffer is specified as 2
        * */
        Observable.just("A", "B", "C", "D", "E", "F")
                .buffer(2)
                .subscribe(new Observer<List<String>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<String> strings) {
                        System.out.println("ObservableBuffer OnNext():");
                        for(String str:strings){
                            System.out.println("ObservableBuffer "+str);
                        }

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void ObservableTimer() {

        Observable.timer(1, TimeUnit.SECONDS)
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Long aLong) {
                        System.out.println("ObservableTimer onNext: " + aLong);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void ObservableRepeat() {

        Observable.range(2,5)
                .repeat(2)
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        System.out.println("ObservableRepeat onNext: " + integer);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void ObservableRange() {


        /*This operator creates an Observable that emits a range of sequential integers.
         The function takes two arguments:
         the starting number and length.*/

        Observable.range(2,5)
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        System.out.println("onNext: " + integer);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void ObservableJust() {

        Observable.just(new String[]{"A", "B", "C", "D", "E", "F"})
                .subscribe(new Observer<String[]>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String[] strings) {
                        System.out.println("ObservableJust: onNext: " + Arrays.toString(strings));
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void ObservableInterval() {

        /*
         * This will print values from 0 after every second.
         */
        Observable.interval(1, TimeUnit.SECONDS)
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Long value) {
                        System.out.println("ObservableInterval nNext: " + value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("onError: " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void ObservableCreate(final List<String> alphabets) {
        /*
         * Observable.create() -> We will need to call the
         * respective methods of the emitter such as onNext()
         * & onComplete() or onError()
         *
         * */
        Observable observable = Observable.create(new ObservableOnSubscribe() {
            @Override
            public void subscribe(ObservableEmitter emitter) {

                try {

                    /*
                     * The emitter can be used to emit each list item
                     * to the subscriber.
                     *
                     * */
                    for (String alphabet : alphabets) {
                        emitter.onNext(alphabet);
                    }

                    /*
                     * Once all the items in the list are emitted,
                     * we can call complete stating that no more items
                     * are to be emitted.
                     *
                     * */
                    emitter.onComplete();

                } catch (Exception e) {

                    /*
                     * If an error occurs in the process,
                     * we can call error.
                     *
                     * */
                    emitter.onError(e);
                }
            }
        });


        /*
         * We create an Observer that is subscribed to Observer.
         * The only function of the Observer in this scenario is
         * to print the valeus emitted by the Observer.
         *
         * */
        Observer observer = new Observer() {
            @Override
            public void onSubscribe(Disposable d) {
                System.out.println("onSubscribe");
            }

            @Override
            public void onNext(Object o) {
                System.out.println("onNext: " + o);
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("onError: " + e.getMessage());
            }

            @Override
            public void onComplete() {
                System.out.println("onComplete");
            }
        };

        /*
         * We can call this method to subscribe
         * the observer to the Observable.
         * */
        observable.subscribe(observer);
    }

    private void ObservableFrom() {

        Observable.fromArray(new String[]{"A", "B", "C", "D", "E", "F"})
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String string) {
                        System.out.println("ObservableFrom OnNext: " + string);
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("onError: " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void launchMap(View view) {

        Intent i = new Intent(this,MapOperatorActivity.class);
        startActivity(i);
    }
}
