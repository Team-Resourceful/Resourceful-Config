package com.teamresourceful.resourcefulconfig.client.components.base;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;

public interface MultiLineLabel {
	MultiLineLabel EMPTY = new MultiLineLabel() {
		@Override
		public void renderLeftAligned(GuiGraphics guiGraphics, int left, int top, int lineHeight, int color) {
		}

		@Nullable
		@Override
		public Style getStyleAtLeftAligned(int i, int j, int k, double d, double e) {
			return null;
		}

		@Override
		public int getLineCount() {
			return 0;
		}

		@Override
		public int getWidth() {
			return 0;
		}
	};

	static MultiLineLabel create(Font font, Component... components) {
		return create(font, Integer.MAX_VALUE, Integer.MAX_VALUE, components);
	}

	static MultiLineLabel create(Font font, int i, Component... components) {
		return create(font, i, Integer.MAX_VALUE, components);
	}

	static MultiLineLabel create(Font font, Component component, int i) {
		return create(font, i, Integer.MAX_VALUE, component);
	}

	static MultiLineLabel create(Font font, int maxWidth, int maxRows, Component... components) {
		return components.length == 0 ? EMPTY : new MultiLineLabel() {
			@Nullable private List<MultiLineLabel.TextAndWidth> cachedTextAndWidth;
			@Nullable private Language splitWithLanguage;

			@Override
			public void renderLeftAligned(GuiGraphics guiGraphics, int left, int top, int lineHeight, int color) {
				int y = top;

				for (var line : this.getSplitMessage()) {
					guiGraphics.drawString(font, line.text(), left, y, color);
					y += lineHeight;
				}
			}

			@Nullable
			@Override
			public Style getStyleAtLeftAligned(int left, int top, int lineHeight, double mouseX, double mouseY) {
				if (mouseX >= left) {
					var lines = this.getSplitMessage();
					int lineIndex = Mth.floor((mouseY - top) / lineHeight);
					if (lineIndex >= 0 && lineIndex < lines.size()) {
						var line = lines.get(lineIndex);
						return font.getSplitter().componentStyleAtWidth(line.text, Mth.floor(mouseX - left));
					}
				}
				return null;
			}

			private List<MultiLineLabel.TextAndWidth> getSplitMessage() {
				var language = Language.getInstance();
                if (this.cachedTextAndWidth == null || language != this.splitWithLanguage) {
                    this.splitWithLanguage = language;
                    List<FormattedText> lines = new ArrayList<>();

                    for (var component : components) {
                        lines.addAll(font.getSplitter().splitLines(component, maxWidth, Style.EMPTY));
                    }

                    this.cachedTextAndWidth = new ArrayList<>();
                    int ix = Math.min(lines.size(), maxRows);
                    List<FormattedText> list2 = lines.subList(0, ix);

                    for (int i = 0; i < list2.size(); i++) {
                        var line = list2.get(i);
                        var lineText = Language.getInstance().getVisualOrder(line);
                        if (i == list2.size() - 1 && ix == maxRows && ix != lines.size()) {
                            FormattedText formattedText2 = font.substrByWidth(line, font.width(line) - font.width(CommonComponents.ELLIPSIS));
                            FormattedText formattedText3 = FormattedText.composite(formattedText2, CommonComponents.ELLIPSIS);
                            this.cachedTextAndWidth.add(new TextAndWidth(Language.getInstance().getVisualOrder(formattedText3), font.width(formattedText3)));
                        } else {
                            this.cachedTextAndWidth.add(new TextAndWidth(lineText, font.width(lineText)));
                        }
                    }

                }
                return this.cachedTextAndWidth;
            }

			@Override
			public int getLineCount() {
				return this.getSplitMessage().size();
			}

			@Override
			public int getWidth() {
				return Math.min(maxWidth, this.getSplitMessage().stream().mapToInt(MultiLineLabel.TextAndWidth::width).max().orElse(0));
			}
		};
	}

	void renderLeftAligned(GuiGraphics guiGraphics, int left, int top, int lineHeight, int color);

	@Nullable
	Style getStyleAtLeftAligned(int i, int j, int k, double d, double e);

	int getLineCount();

	int getWidth();

	record TextAndWidth(FormattedCharSequence text, int width) {
	}
}