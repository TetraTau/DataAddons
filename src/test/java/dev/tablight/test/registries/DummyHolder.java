/*
 * Any copyright is dedicated to the Public Domain.
 * https://creativecommons.org/publicdomain/zero/1.0/
 */

package dev.tablight.test.registries;

import dev.tablight.dataaddon.annotation.group.Holder;
import dev.tablight.dataaddon.holder.eventsourcing.ConcurrentEventTypeHolder;

@Holder("dummyGroup")
public class DummyHolder extends ConcurrentEventTypeHolder {}
