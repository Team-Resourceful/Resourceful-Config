package com.teamresourceful.resourcefulconfig.client.utils;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

import java.util.Locale;

@SuppressWarnings("NoTranslation")
public class KeyCodeHelper {

    private static final Int2ObjectMap<Component> HARD_CODED_KEYCODES = new Int2ObjectOpenHashMap<>();

    public static Component getKeyName(int key) {
        if (key < 0) {
            return HARD_CODED_KEYCODES.computeIfAbsent(
                    key,
                    $ -> {
                        int button = -key - 100;
                        return Component.translatable("key.mouse", button + 1);
                    }
            );
        }
        return HARD_CODED_KEYCODES.computeIfAbsent(
                key,
                $ -> {
                    String keyName = GLFW.glfwGetKeyName(key, 0);
                    if (keyName == null) {
                        return Component.literal("???");
                    }
                    return Component.literal(keyName.toLowerCase(Locale.ROOT));
                }
        );
    }

    public static boolean isMouseKeyPressed(int button) {
        Minecraft mc = Minecraft.getInstance();
        return GLFW.glfwGetMouseButton(mc.getWindow().getWindow(), button) == GLFW.GLFW_PRESS;
    }

    static {
        HARD_CODED_KEYCODES.put(-100, Component.translatable("key.mouse.left"));
        HARD_CODED_KEYCODES.put(-101, Component.translatable("key.mouse.right"));
        HARD_CODED_KEYCODES.put(-102, Component.translatable("key.mouse.middle"));
        HARD_CODED_KEYCODES.put(-103, Component.translatableWithFallback("key.mouse.4", "Mouse 4"));
        HARD_CODED_KEYCODES.put(-104, Component.translatableWithFallback("key.mouse.5", "Mouse 5"));
        HARD_CODED_KEYCODES.put(-105, Component.translatableWithFallback("key.mouse.6", "Mouse 6"));
        HARD_CODED_KEYCODES.put(-106, Component.translatableWithFallback("key.mouse.7", "Mouse 7"));
        HARD_CODED_KEYCODES.put(-107, Component.translatableWithFallback("key.mouse.8", "Mouse 8"));
        HARD_CODED_KEYCODES.put(48, Component.translatableWithFallback("key.keyboard.0", "0"));
        HARD_CODED_KEYCODES.put(49, Component.translatableWithFallback("key.keyboard.1", "1"));
        HARD_CODED_KEYCODES.put(50, Component.translatableWithFallback("key.keyboard.2", "2"));
        HARD_CODED_KEYCODES.put(51, Component.translatableWithFallback("key.keyboard.3", "3"));
        HARD_CODED_KEYCODES.put(52, Component.translatableWithFallback("key.keyboard.4", "4"));
        HARD_CODED_KEYCODES.put(53, Component.translatableWithFallback("key.keyboard.5", "5"));
        HARD_CODED_KEYCODES.put(54, Component.translatableWithFallback("key.keyboard.6", "6"));
        HARD_CODED_KEYCODES.put(55, Component.translatableWithFallback("key.keyboard.7", "7"));
        HARD_CODED_KEYCODES.put(56, Component.translatableWithFallback("key.keyboard.8", "8"));
        HARD_CODED_KEYCODES.put(57, Component.translatableWithFallback("key.keyboard.9", "9"));
        HARD_CODED_KEYCODES.put(65, Component.translatableWithFallback("key.keyboard.a", "A"));
        HARD_CODED_KEYCODES.put(66, Component.translatableWithFallback("key.keyboard.b", "B"));
        HARD_CODED_KEYCODES.put(67, Component.translatableWithFallback("key.keyboard.c", "C"));
        HARD_CODED_KEYCODES.put(68, Component.translatableWithFallback("key.keyboard.d", "D"));
        HARD_CODED_KEYCODES.put(69, Component.translatableWithFallback("key.keyboard.e", "E"));
        HARD_CODED_KEYCODES.put(70, Component.translatableWithFallback("key.keyboard.f", "F"));
        HARD_CODED_KEYCODES.put(71, Component.translatableWithFallback("key.keyboard.g", "G"));
        HARD_CODED_KEYCODES.put(72, Component.translatableWithFallback("key.keyboard.h", "H"));
        HARD_CODED_KEYCODES.put(73, Component.translatableWithFallback("key.keyboard.i", "I"));
        HARD_CODED_KEYCODES.put(74, Component.translatableWithFallback("key.keyboard.j", "J"));
        HARD_CODED_KEYCODES.put(75, Component.translatableWithFallback("key.keyboard.k", "K"));
        HARD_CODED_KEYCODES.put(76, Component.translatableWithFallback("key.keyboard.l", "L"));
        HARD_CODED_KEYCODES.put(77, Component.translatableWithFallback("key.keyboard.m", "M"));
        HARD_CODED_KEYCODES.put(78, Component.translatableWithFallback("key.keyboard.n", "N"));
        HARD_CODED_KEYCODES.put(79, Component.translatableWithFallback("key.keyboard.o", "O"));
        HARD_CODED_KEYCODES.put(80, Component.translatableWithFallback("key.keyboard.p", "P"));
        HARD_CODED_KEYCODES.put(81, Component.translatableWithFallback("key.keyboard.q", "Q"));
        HARD_CODED_KEYCODES.put(82, Component.translatableWithFallback("key.keyboard.r", "R"));
        HARD_CODED_KEYCODES.put(83, Component.translatableWithFallback("key.keyboard.s", "S"));
        HARD_CODED_KEYCODES.put(84, Component.translatableWithFallback("key.keyboard.t", "T"));
        HARD_CODED_KEYCODES.put(85, Component.translatableWithFallback("key.keyboard.u", "U"));
        HARD_CODED_KEYCODES.put(86, Component.translatableWithFallback("key.keyboard.v", "V"));
        HARD_CODED_KEYCODES.put(87, Component.translatableWithFallback("key.keyboard.w", "W"));
        HARD_CODED_KEYCODES.put(88, Component.translatableWithFallback("key.keyboard.x", "X"));
        HARD_CODED_KEYCODES.put(89, Component.translatableWithFallback("key.keyboard.y", "Y"));
        HARD_CODED_KEYCODES.put(90, Component.translatableWithFallback("key.keyboard.z", "Z"));
        HARD_CODED_KEYCODES.put(290, Component.translatable("key.keyboard.f1"));
        HARD_CODED_KEYCODES.put(291, Component.translatable("key.keyboard.f2"));
        HARD_CODED_KEYCODES.put(292, Component.translatable("key.keyboard.f3"));
        HARD_CODED_KEYCODES.put(293, Component.translatable("key.keyboard.f4"));
        HARD_CODED_KEYCODES.put(294, Component.translatable("key.keyboard.f5"));
        HARD_CODED_KEYCODES.put(295, Component.translatable("key.keyboard.f6"));
        HARD_CODED_KEYCODES.put(296, Component.translatable("key.keyboard.f7"));
        HARD_CODED_KEYCODES.put(297, Component.translatable("key.keyboard.f8"));
        HARD_CODED_KEYCODES.put(298, Component.translatable("key.keyboard.f9"));
        HARD_CODED_KEYCODES.put(299, Component.translatable("key.keyboard.f10"));
        HARD_CODED_KEYCODES.put(300, Component.translatable("key.keyboard.f11"));
        HARD_CODED_KEYCODES.put(301, Component.translatable("key.keyboard.f12"));
        HARD_CODED_KEYCODES.put(302, Component.translatable("key.keyboard.f13"));
        HARD_CODED_KEYCODES.put(303, Component.translatable("key.keyboard.f14"));
        HARD_CODED_KEYCODES.put(304, Component.translatable("key.keyboard.f15"));
        HARD_CODED_KEYCODES.put(305, Component.translatable("key.keyboard.f16"));
        HARD_CODED_KEYCODES.put(306, Component.translatable("key.keyboard.f17"));
        HARD_CODED_KEYCODES.put(307, Component.translatable("key.keyboard.f18"));
        HARD_CODED_KEYCODES.put(308, Component.translatable("key.keyboard.f19"));
        HARD_CODED_KEYCODES.put(309, Component.translatable("key.keyboard.f20"));
        HARD_CODED_KEYCODES.put(310, Component.translatable("key.keyboard.f21"));
        HARD_CODED_KEYCODES.put(311, Component.translatable("key.keyboard.f22"));
        HARD_CODED_KEYCODES.put(312, Component.translatable("key.keyboard.f23"));
        HARD_CODED_KEYCODES.put(313, Component.translatable("key.keyboard.f24"));
        HARD_CODED_KEYCODES.put(314, Component.translatable("key.keyboard.f25"));
        HARD_CODED_KEYCODES.put(282, Component.translatable("key.keyboard.num.lock"));
        HARD_CODED_KEYCODES.put(320, Component.translatable("key.keyboard.keypad.0"));
        HARD_CODED_KEYCODES.put(321, Component.translatable("key.keyboard.keypad.1"));
        HARD_CODED_KEYCODES.put(322, Component.translatable("key.keyboard.keypad.2"));
        HARD_CODED_KEYCODES.put(323, Component.translatable("key.keyboard.keypad.3"));
        HARD_CODED_KEYCODES.put(324, Component.translatable("key.keyboard.keypad.4"));
        HARD_CODED_KEYCODES.put(325, Component.translatable("key.keyboard.keypad.5"));
        HARD_CODED_KEYCODES.put(326, Component.translatable("key.keyboard.keypad.6"));
        HARD_CODED_KEYCODES.put(327, Component.translatable("key.keyboard.keypad.7"));
        HARD_CODED_KEYCODES.put(328, Component.translatable("key.keyboard.keypad.8"));
        HARD_CODED_KEYCODES.put(329, Component.translatable("key.keyboard.keypad.9"));
        HARD_CODED_KEYCODES.put(334, Component.translatable("key.keyboard.keypad.add"));
        HARD_CODED_KEYCODES.put(330, Component.translatable("key.keyboard.keypad.decimal"));
        HARD_CODED_KEYCODES.put(335, Component.translatable("key.keyboard.keypad.enter"));
        HARD_CODED_KEYCODES.put(336, Component.translatable("key.keyboard.keypad.equal"));
        HARD_CODED_KEYCODES.put(332, Component.translatable("key.keyboard.keypad.multiply"));
        HARD_CODED_KEYCODES.put(331, Component.translatable("key.keyboard.keypad.divide"));
        HARD_CODED_KEYCODES.put(333, Component.translatable("key.keyboard.keypad.subtract"));
        HARD_CODED_KEYCODES.put(264, Component.translatable("key.keyboard.down"));
        HARD_CODED_KEYCODES.put(263, Component.translatable("key.keyboard.left"));
        HARD_CODED_KEYCODES.put(262, Component.translatable("key.keyboard.right"));
        HARD_CODED_KEYCODES.put(265, Component.translatable("key.keyboard.up"));
        HARD_CODED_KEYCODES.put(39, Component.translatable("key.keyboard.apostrophe"));
        HARD_CODED_KEYCODES.put(92, Component.translatable("key.keyboard.backslash"));
        HARD_CODED_KEYCODES.put(44, Component.translatable("key.keyboard.comma"));
        HARD_CODED_KEYCODES.put(61, Component.translatable("key.keyboard.equal"));
        HARD_CODED_KEYCODES.put(96, Component.translatable("key.keyboard.grave.accent"));
        HARD_CODED_KEYCODES.put(91, Component.translatable("key.keyboard.left.bracket"));
        HARD_CODED_KEYCODES.put(45, Component.translatable("key.keyboard.minus"));
        HARD_CODED_KEYCODES.put(46, Component.translatable("key.keyboard.period"));
        HARD_CODED_KEYCODES.put(93, Component.translatable("key.keyboard.right.bracket"));
        HARD_CODED_KEYCODES.put(59, Component.translatable("key.keyboard.semicolon"));
        HARD_CODED_KEYCODES.put(47, Component.translatable("key.keyboard.slash"));
        HARD_CODED_KEYCODES.put(32, Component.translatable("key.keyboard.space"));
        HARD_CODED_KEYCODES.put(258, Component.translatable("key.keyboard.tab"));
        HARD_CODED_KEYCODES.put(342, Component.translatable("key.keyboard.left.alt"));
        HARD_CODED_KEYCODES.put(341, Component.translatable("key.keyboard.left.control"));
        HARD_CODED_KEYCODES.put(340, Component.translatable("key.keyboard.left.shift"));
        HARD_CODED_KEYCODES.put(343, Component.translatable("key.keyboard.left.win"));
        HARD_CODED_KEYCODES.put(346, Component.translatable("key.keyboard.right.alt"));
        HARD_CODED_KEYCODES.put(345, Component.translatable("key.keyboard.right.control"));
        HARD_CODED_KEYCODES.put(344, Component.translatable("key.keyboard.right.shift"));
        HARD_CODED_KEYCODES.put(347, Component.translatable("key.keyboard.right.win"));
        HARD_CODED_KEYCODES.put(257, Component.translatable("key.keyboard.enter"));
        HARD_CODED_KEYCODES.put(256, Component.translatable("key.keyboard.escape"));
        HARD_CODED_KEYCODES.put(259, Component.translatable("key.keyboard.backspace"));
        HARD_CODED_KEYCODES.put(261, Component.translatable("key.keyboard.delete"));
        HARD_CODED_KEYCODES.put(269, Component.translatable("key.keyboard.end"));
        HARD_CODED_KEYCODES.put(268, Component.translatable("key.keyboard.home"));
        HARD_CODED_KEYCODES.put(260, Component.translatable("key.keyboard.insert"));
        HARD_CODED_KEYCODES.put(267, Component.translatable("key.keyboard.page.down"));
        HARD_CODED_KEYCODES.put(266, Component.translatable("key.keyboard.page.up"));
        HARD_CODED_KEYCODES.put(280, Component.translatable("key.keyboard.caps.lock"));
        HARD_CODED_KEYCODES.put(284, Component.translatable("key.keyboard.pause"));
        HARD_CODED_KEYCODES.put(281, Component.translatable("key.keyboard.scroll.lock"));
        HARD_CODED_KEYCODES.put(348, Component.translatable("key.keyboard.menu"));
        HARD_CODED_KEYCODES.put(283, Component.translatable("key.keyboard.print.screen"));
        HARD_CODED_KEYCODES.put(161, Component.translatable("key.keyboard.world.1"));
        HARD_CODED_KEYCODES.put(162, Component.translatable("key.keyboard.world.2"));
    }
}
