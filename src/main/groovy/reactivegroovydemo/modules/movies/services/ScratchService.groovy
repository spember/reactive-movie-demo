package reactivegroovydemo.modules.movies.services

import groovy.util.logging.Slf4j
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription
import ratpack.rx.RxRatpack
import rx.Observable

/**
 * @author Steve Pember
 */
@Slf4j
class ScratchService {

//    private Observable<Integer> numbers = Observable.create({subscriber ->
//        try {
//            List numbers = 1..100
//            numbers.each {Integer num->
//                subscriber.onNext(num)
//                // once subscriber has finished with one number, it asks for more
//            }
//            // after sending all values we complete the sequence
//            if (!subscriber.unsubscribed) {
//                subscriber.onCompleted()
//            }
//        }
//        catch(Exception e) { //catch all. Gross, I know
//            if (!subscriber.isUnsubscribed()){
//                subscriber.onError(e)
//            }
//        }
//    } as Observable.OnSubscribe)// so IntelliJ is happy
//
//    private Subscriber<Integer> subscriber = new RxRatpack.ExecutionBackedSubscriber<Integer>() {
//        @Override
//        void onSubscribe(Subscription subscription) {
//
//        }
//
//        @Override
//        void onNext(Integer integer) {
//
//        }
//
//        @Override
//        void onError(Throwable throwable) {
//
//        }
//
//        @Override
//        void onComplete() {
//
//        }
//    }
//
//
    Object test() {
        numbers.subscribe(

        new rx.Subscriber<Integer>() {
            @Override
            void onCompleted() {
                log.error("All done!")
            }

            @Override
            void onError(Throwable e) {
                log.error("Ooops!", e)
            }

            @Override
            void onNext(Integer integer) {
                // do some work on the incoming Integer
                log.info("Received integer ${integer}")
                // after work is complete, call:
                request(1)
                // this value doesn't have to be 1. I can ask for varying
                // number items based on current resources
            }

            @Override
            void onStart() {
                // ask the Observer to send 1 item to begin with
                request(1)
            }
        }

        );

        int result = 0
        numbers
        .subscribe({Integer data ->
            println "Received ${data}"
            result += data

        })
        result
    }
}
