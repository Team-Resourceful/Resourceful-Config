package com.teamresourceful.resourcefulconfig.api.types.info;

import com.teamresourceful.resourcefulconfig.api.types.options.TranslatableValue;

public interface ResourcefulConfigInfoButton {

    String icon();

    TranslatableValue text();

    void onClick();

    static ResourcefulConfigInfoButton create(
            String icon,
            TranslatableValue text,
            Runnable onClick
    ) {
        return new ResourcefulConfigInfoButton() {

            @Override
            public String icon() {
                return icon;
            }

            @Override
            public TranslatableValue text() {
                return text;
            }

            @Override
            public void onClick() {
                onClick.run();
            }
        };
    }
}
