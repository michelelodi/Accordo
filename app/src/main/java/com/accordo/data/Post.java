package com.accordo.data;

import org.jetbrains.annotations.NotNull;

public interface Post {

    void setContent(String content);

    @NotNull String toString();
}
