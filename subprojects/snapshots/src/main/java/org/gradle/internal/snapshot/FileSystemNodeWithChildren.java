/*
 * Copyright 2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gradle.internal.snapshot;

import java.util.List;
import java.util.Optional;

public class FileSystemNodeWithChildren extends AbstractFileSystemNodeWithChildren {

    public FileSystemNodeWithChildren(String prefix, List<? extends FileSystemNode> children) {
        super(prefix, children);
        assert !children.isEmpty();
    }

    @Override
    protected Optional<MetadataSnapshot> getThisSnapshot() {
        return Optional.empty();
    }

    @Override
    protected FileSystemNode createCopy(String prefix, List<? extends FileSystemNode> merged) {
        return new FileSystemNodeWithChildren(prefix, merged);
    }

    @Override
    protected Optional<FileSystemNode> withNoChildren() {
        return Optional.empty();
    }

    @Override
    protected FileSystemNode withUnkownChildInvalidated() {
        return this;
    }

}