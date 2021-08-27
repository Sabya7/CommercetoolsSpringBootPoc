package com.sabyacommercetools.marut.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class GroupingCollector<T> implements Collector<T, List<List<T>>, List<List<T>>> {
    private final int elementCountInGroup;

    public GroupingCollector(int elementCountInGroup) {
        this.elementCountInGroup = elementCountInGroup;
    }

    @Override
    public Supplier<List<List<T>>> supplier() {
        return ArrayList::new;
    }

    @Override
    public BiConsumer<List<List<T>>, T> accumulator() {
        return (lists, integer) -> {
            if (!lists.isEmpty()) {
                List<T> integers = lists.get(lists.size() - 1);
                if (integers.size() < elementCountInGroup) {
                    integers.add(integer);
                    return;
                }
            }

            List<T> list = new ArrayList<>();
            list.add(integer);
            lists.add(list);
        };
    }

    @Override
    public BinaryOperator<List<List<T>>> combiner() {
        return (lists, lists2) -> {
            List<List<T>> r = new ArrayList<>();
            r.addAll(lists);
            r.addAll(lists2);
            return r;
        };
    }

    @Override
    public Function<List<List<T>>, List<List<T>>> finisher() {
        return lists -> lists;
    }

    @Override
    public Set<Characteristics> characteristics() {
        return Collections.emptySet();
    }
}