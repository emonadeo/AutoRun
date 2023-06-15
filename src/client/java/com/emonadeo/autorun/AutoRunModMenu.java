package com.emonadeo.autorun;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class AutoRunModMenu implements ModMenuApi, ConfigScreenFactory<Screen> {
    @Override
    public ConfigScreenFactory<Screen> getModConfigScreenFactory() {
        return this;
    }

    @Override
    public Screen create(Screen screen) {
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(screen)
                .setTitle(Text.translatable("title." + AutoRunMod.MODID + ".config"));

        ConfigEntryBuilder entryBuilder = builder.entryBuilder();
        ConfigCategory general = builder.getOrCreateCategory(Text.translatable("config." + AutoRunMod.MODID + ".general"));

        // Toogle Auto-Jump
        general.addEntry(entryBuilder.startBooleanToggle(Text.translatable("config." + AutoRunMod.MODID + ".toggleAutoJump"), AutoRunMod.isToggleAutoJump())
                .setDefaultValue(true)
                .setTooltip(Text.translatable("config." + AutoRunMod.MODID + ".toggleAutoJump.description"))
                .setSaveConsumer(AutoRunMod::setToggleAutoJump)
                .build());

        // Delay Buffer
        general.addEntry(entryBuilder.startIntField(Text.translatable("config." + AutoRunMod.MODID + ".delayBuffer"), AutoRunMod.getDelayBuffer())
                .setDefaultValue(20)
                .setTooltip(Text.translatable("config." + AutoRunMod.MODID + ".delayBuffer.description"))
                .setSaveConsumer(AutoRunMod::setDelayBuffer)
                .build());

        return builder.setSavingRunnable(() -> {
            AutoRunMod.saveConfig(AutoRunMod.CFG_FILE);
            AutoRunMod.loadConfig(AutoRunMod.CFG_FILE);
        }).build();
    }
}
