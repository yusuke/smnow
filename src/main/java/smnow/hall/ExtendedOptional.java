package smnow.hall;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Copyright 2014Shinya Mochida
 * <p>
 * Licensed under the Apache License,Version2.0(the"License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing,software
 * Distributed under the License is distributed on an"AS IS"BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class ExtendedOptional<T> {

    private final Optional<T> optional;

    private ExtendedOptional() {
        this.optional = Optional.empty();
    }

    private ExtendedOptional(T value) {
        this.optional = Optional.ofNullable(value);
    }

    public static <T> ExtendedOptional<T> of(T value) {
        Objects.requireNonNull(value);
        return new ExtendedOptional<>(value);
    }

    public static <T> ExtendedOptional<T> ofNullable(T value) {
        return value == null? empty() : of(value);
    }

    public static <T> ExtendedOptional<T> empty() {
        return new ExtendedOptional<>();
    }

    public Optional<T> filter(Predicate<? super T> predicate) {
        return optional.filter(predicate);
    }

    public <U> Optional<U> flatMap(Function<? super T, Optional<U>> mapper) {
        return optional.flatMap(mapper);
    }

    public T get() {
        return optional.get();
    }

    public ExtendedOptional<T> ifPresent(Consumer<? super T> consumer) {
        optional.ifPresent(consumer);
        return this;
    }

    public boolean isPresent() {
        return optional.isPresent();
    }

    public <U> Optional<U> map(Function<? super T, ? extends U> mapper) {
        return optional.map(mapper);
    }

    public T orElseGet(Supplier<? extends T> other) {
        return optional.orElseGet(other);
    }

    public <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X {
        return optional.orElseThrow(exceptionSupplier);
    }

    public ExtendedOptional<T> ifNotPresent(VoidConsumer consumer) {
        if (!optional.isPresent()) consumer.accept();
        return this;
    }

    @FunctionalInterface
    public interface VoidConsumer {
        public void accept();
    }

}