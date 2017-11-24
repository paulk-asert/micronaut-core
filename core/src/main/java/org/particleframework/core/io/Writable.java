/*
 * Copyright 2017 original authors
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 */
package org.particleframework.core.io;

import org.particleframework.core.annotation.Nullable;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * <p>An interface for classes to implement that are capable of writing themselves to a {@link Writer}</p>
 *
 * @author Graeme Rocher
 * @since 1.0
 */
public interface Writable extends Streamable {
    /**
     * Writes this object to the given writer.
     *
     * @param out the Writer to which this Writable should output its data.
     * @throws IOException if an error occurred while outputting data to the writer
     */
    void writeTo(Writer out) throws IOException;

    /**
     * Write this object to the given {@link OutputStream} using {@link StandardCharsets#UTF_8} by default
     *
     * @param outputStream The output stream
     * @throws IOException if an error occurred while outputting data to the writer
     */
    default void writeTo(OutputStream outputStream) throws IOException {
        writeTo(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
    }

    /**
     * Write this {@link Writable} to the given {@link File}
     * @param file The file
     * @throws IOException if an error occurred while outputting data to the writer
     */
    default void writeTo(File file) throws IOException {
        try(FileOutputStream outputStream = new FileOutputStream(file)) {
            writeTo(outputStream);
        }
    }
     /**
     * Write this object to the given {@link OutputStream} using {@link StandardCharsets#UTF_8} by default
     *
     * @param outputStream The output stream
     * @param charset The charset to use. Defaults to {@link StandardCharsets#UTF_8}
     * @throws IOException if an error occurred while outputting data to the writer
     */
    default void writeTo(OutputStream outputStream, @Nullable Charset charset) throws IOException {
        writeTo(new OutputStreamWriter(outputStream, charset == null ? StandardCharsets.UTF_8 : charset));
    }
}