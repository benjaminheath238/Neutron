package com.bjmh.lib.io.config;

@FunctionalInterface
public interface ConfigConsumer {
  public void accept(ConfigNode node);
}
