package org.soulasphyxia.webcourseproject.utils;

import org.springframework.http.MediaType;

import java.io.InputStream;

public record ResourceObject(String name, InputStream stream, MediaType contentType) {}
