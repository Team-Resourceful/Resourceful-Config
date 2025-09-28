package com.teamresourceful.resourcefulconfig.demo;

import com.teamresourceful.resourcefulconfig.api.types.info.TooltipProvider;
import net.minecraft.network.chat.Component;

public enum DemoEnum implements TooltipProvider {
    FIRST {
        @Override
        public String getTranslation() {
            return "demo.first";
        }

        @Override
        public Component getTooltip() {
            return Component.literal("first option");
        }
    },
    SECOND {
        @Override
        public String getTranslation() {
            return "demo.second";
        }

        @Override
        public Component getTooltip() {
            return Component.literal("second option");
        }
    },
    THIRD {
        @Override
        public String getTranslation() {
            return "demo.third";
        }

        @Override
        public Component getTooltip() {
            return Component.literal("third option\nwith a newline even!");
        }
    };

    public abstract String getTranslation();
}
