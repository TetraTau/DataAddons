/*
 * Any copyright is dedicated to the Public Domain.
 * https://creativecommons.org/publicdomain/zero/1.0/
 */

package dev.tablight.test.registries.subpkg;

import dev.tablight.common.base.dataaddon.annotation.group.Controller;
import dev.tablight.common.base.dataaddon.storeload.DefaultStoreLoadController;

@Controller("dummyGroup")
public class DummyController extends DefaultStoreLoadController {}
