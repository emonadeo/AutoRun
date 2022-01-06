package com.emonadeo.autorun;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.TranslatableText;

public class AutoRunModMenu implements ModMenuApi, ConfigScreenFactory<Screen> {
    @Override
    public ConfigScreenFactory<Screen> getModConfigScreenFactory() {
        return this;
    }

    @Override
    public Screen create(Screen screen) {
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(screen)
                .setTitle(new TranslatableText("title." + AutoRun.MODID + ".config"));

        ConfigEntryBuilder entryBuilder = builder.entryBuilder();
        ConfigCategory general = builder.getOrCreateCategory(new TranslatableText("config." + AutoRun.MODID + ".general"));

        // Toogle Auto-Jump
        general.addEntry(entryBuilder.startBooleanToggle(new TranslatableText("config." + AutoRun.MODID + ".toggleAutoJump"), AutoRun.isToggleAutoJump())
                .setDefaultValue(true)
                .setTooltip(new TranslatableText("config." + AutoRun.MODID + ".toggleAutoJump.description"))
                .setSaveConsumer(AutoRun::setToggleAutoJump)
                .build());

        // Delay Buffer
        general.addEntry(entryBuilder.startIntField(new TranslatableText("config." + AutoRun.MODID + ".delayBuffer"), AutoRun.getDelayBuffer())
                .setDefaultValue(20)
                .setTooltip(new TranslatableText("config." + AutoRun.MODID + ".delayBuffer.description"))
                .setSaveConsumer(AutoRun::setDelayBuffer)
                .build());

        return builder.setSavingRunnable(() -> {
            AutoRun.saveConfig(AutoRun.CFG_FILE);
            AutoRun.loadConfig(AutoRun.CFG_FILE);
        }).build();
    }
}
