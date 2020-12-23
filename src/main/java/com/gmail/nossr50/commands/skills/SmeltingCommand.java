package com.gmail.nossr50.commands.skills;

import com.gmail.nossr50.datatypes.skills.SubSkillType;
import com.gmail.nossr50.locale.LocaleLoader;
import com.gmail.nossr50.util.Permissions;
import com.gmail.nossr50.util.skills.RankUtils;
import com.gmail.nossr50.util.skills.SkillActivationType;
import com.gmail.nossr50.util.text.TextComponentFactory;
import com.neetgames.mcmmo.player.OnlineMMOPlayer;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SmeltingCommand extends SkillCommand {
    private String burnTimeModifier;
    private String str_secondSmeltChance;
    private String str_secondSmeltChanceLucky;
    private String str_fluxMiningChance;
    private String str_fluxMiningChanceLucky;

    private boolean canFuelEfficiency;
    private boolean canSecondSmelt;
    private boolean canFluxMine;
    private boolean canUnderstandTheArt;

    public SmeltingCommand() {
        super(CoreSkills.SMELTING);
    }

    @Override
    protected void dataCalculations(@NotNull OnlineMMOPlayer mmoPlayer, float skillValue) {
        // FUEL EFFICIENCY
        if (canFuelEfficiency) {
            burnTimeModifier = String.valueOf(((McMMOPlayer) (mmoPlayer)).getSmeltingManager().getFuelEfficiencyMultiplier());
        }

        // FLUX MINING
        /*if (canFluxMine) {
            String[] fluxMiningStrings = getAbilityDisplayValues(SkillActivationType.RANDOM_LINEAR_100_SCALE_WITH_CAP, player, SubSkillType.SMELTING_FLUX_MINING);
            str_fluxMiningChance = fluxMiningStrings[0];
            str_fluxMiningChanceLucky = fluxMiningStrings[1];
        }*/
        
        // SECOND SMELT
        if (canSecondSmelt) {
            String[] secondSmeltStrings = getAbilityDisplayValues(SkillActivationType.RANDOM_LINEAR_100_SCALE_WITH_CAP, mmoPlayer, SubSkillType.SMELTING_SECOND_SMELT);
            str_secondSmeltChance = secondSmeltStrings[0];
            str_secondSmeltChanceLucky = secondSmeltStrings[1];
        }
    }

    @Override
    protected void permissionsCheck(@NotNull OnlineMMOPlayer mmoPlayer) {
        canFuelEfficiency = canUseSubskill(mmoPlayer, SubSkillType.SMELTING_FUEL_EFFICIENCY);
        canSecondSmelt = canUseSubskill(mmoPlayer, SubSkillType.SMELTING_SECOND_SMELT);
        //canFluxMine = canUseSubskill(player, SubSkillType.SMELTING_FLUX_MINING);
        canUnderstandTheArt = Permissions.vanillaXpBoost(Misc.adaptPlayer(mmoPlayer), rootSkill) && RankUtils.hasUnlockedSubskill(mmoPlayer, SubSkillType.SMELTING_UNDERSTANDING_THE_ART);
    }

    @Override
    protected @NotNull List<String> statsDisplay(@NotNull OnlineMMOPlayer mmoPlayer, float skillValue, boolean hasEndurance, boolean isLucky) {
        List<String> messages = new ArrayList<>();

        /*if (canFluxMine) {
            messages.add(getStatMessage(SubSkillType.SMELTING_FLUX_MINING, str_fluxMiningChance)
                    + (isLucky ? LocaleLoader.getString("Perks.Lucky.Bonus", str_fluxMiningChanceLucky) : ""));
            //messages.add(LocaleLoader.getString("Smelting.Ability.FluxMining", str_fluxMiningChance) + (isLucky ? LocaleLoader.getString("Perks.Lucky.Bonus", str_fluxMiningChanceLucky) : ""));
        }*/
        
        if (canFuelEfficiency) {
            messages.add(getStatMessage(false, true, SubSkillType.SMELTING_FUEL_EFFICIENCY, burnTimeModifier));
        }

        if (canSecondSmelt) {
            messages.add(getStatMessage(SubSkillType.SMELTING_SECOND_SMELT, str_secondSmeltChance)
                    + (isLucky ? LocaleLoader.getString("Perks.Lucky.Bonus", str_secondSmeltChanceLucky) : ""));
        }

        if (canUnderstandTheArt) {
            messages.add(getStatMessage(false, true, SubSkillType.SMELTING_UNDERSTANDING_THE_ART,
                    String.valueOf(((McMMOPlayer) (mmoPlayer)).getSmeltingManager().getVanillaXpMultiplier())));
        }

        return messages;
    }

    @Override
    protected @NotNull List<Component> getTextComponents(@NotNull OnlineMMOPlayer mmoPlayer) {
        List<Component> textComponents = new ArrayList<>();

        TextComponentFactory.getSubSkillTextComponents(mmoPlayer, textComponents, CoreSkills.SMELTING);

        return textComponents;
    }
}
