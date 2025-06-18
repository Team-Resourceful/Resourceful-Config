package com.teamresourceful.resourcefulconfig.demo;

public enum DemoEnum {
    FIRST {
        @Override
        public String getTranslation() {
            return "demo.first";
        }
    },
    SECOND {
        @Override
        public String getTranslation() {
            return "demo.second";
        }
    },
    THIRD {
        @Override
        public String getTranslation() {
            return "demo.third";
        }
    };

    public abstract String getTranslation();
}
