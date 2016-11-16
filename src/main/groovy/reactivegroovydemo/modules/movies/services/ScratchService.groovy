package reactivegroovydemo.modules.movies.services

import groovy.util.logging.Slf4j
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.functions.Function
import io.reactivex.functions.Predicate
import ratpack.service.Service
import ratpack.service.StartEvent


/**
 * @author Steve Pember
 */
@Slf4j
class ScratchService implements Service {

    void sample() {
        // examples of rxJava 2.0

        //specific fromX() methods instead of from(X)
        Observable.fromIterable(1..100)
        .map(new Function<Integer, Integer>() {
            // note Function instead of Func1.
            // Still not Java 8 Function though
            @Override
            Integer apply(Integer num) throws Exception {
                num * num
            }
        })
        .filter(new Predicate<Integer>() {
            // filter takes a specific Predicate object instead of
            // Func1<Integer, Boolean>
            @Override
            boolean test(Integer num) throws Exception {
                num %2 == 0
            }
        })
        .subscribe(
                {log.info("Received ${it}")},
                {log.error("Whoops:", it)},
                {log.info("All done")}
        )


        Flowable.fromIterable(1..100)
        .map({Integer i ->
            i * i
        } as Function<Integer, Integer>)
        .filter({Integer i -> i % 2 == 0} as Predicate<Integer>)

        .subscribe({log.info("Also received: ${it}")})

    }

    @Override
    void onStart(StartEvent event) throws Exception {
        //sample()
    }
}
