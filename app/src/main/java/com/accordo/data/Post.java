package com.accordo.data;

import org.jetbrains.annotations.NotNull;

public interface Post {

    String getAuthor();

    String getContent();

    void setContent(String content);

    @NotNull String toString();
}
