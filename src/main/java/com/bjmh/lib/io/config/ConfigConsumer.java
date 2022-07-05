package com.bjmh.lib.io.config;

import java.util.function.Consumer;

@FunctionalInterface
public interface ConfigConsumer extends Consumer<ConfigNode> {
  @Override
  public void accept(ConfigNode node);
}
