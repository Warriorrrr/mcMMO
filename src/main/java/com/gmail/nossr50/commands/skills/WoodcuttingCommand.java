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

public class WoodcuttingCommand extends SkillCommand {
    private String treeFellerLength;
    private String treeFellerLengthEndurance;
    private String doubleDropChance;
    private String doubleDropChanceLucky;

    private boolean canTreeFell;
    private boolean canLeafBlow;
    private boolean canDoubleDrop;
    private boolean canKnockOnWood;
    private boolean canSplinter;
    private boolean canBarkSurgeon;
    private boolean canNaturesBounty;

    public WoodcuttingCommand() {
        super(CoreSkills.WOODCUTTING);
    }

    @Override
    protected void dataCalculations(@NotNull OnlineMMOPlayer mmoPlayer, float skillValue) {
        // DOUBLE DROPS
        if (canDoubleDrop) {
            setDoubleDropClassicChanceStrings(mmoPlayer);
        }
        
        // TREE FELLER
        if (canTreeFell) {
            String[] treeFellerStrings = calculateLengthDisplayValues(mmoPlayer, skillValue);
            treeFellerLength = treeFellerStrings[0];
            treeFellerLengthEndurance = treeFellerStrings[1];
        }
    }

    private void setDoubleDropClassicChanceStrings(OnlineMMOPlayer mmoPlayer) {
        String[] doubleDropStrings = getAbilityDisplayValues(SkillActivationType.RANDOM_LINEAR_100_SCALE_WITH_CAP, mmoPlayer, SubSkillType.WOODCUTTING_HARVEST_LUMBER);
        doubleDropChance = doubleDropStrings[0];
        doubleDropChanceLucky = doubleDropStrings[1];
    }

    @Override
    protected void permissionsCheck(@NotNull OnlineMMOPlayer mmoPlayer) {
        canTreeFell = RankUtils.hasUnlockedSubskill(mmoPlayer, SubSkillType.WOODCUTTING_TREE_FELLER) && Permissions.treeFeller(mmoPlayer.getPlayer());
        canDoubleDrop = canUseSubskill(mmoPlayer, SubSkillType.WOODCUTTING_HARVEST_LUMBER) && !rootSkill.getDoubleDropsDisabled() && RankUtils.getRank(mmoPlayer, SubSkillType.WOODCUTTING_HARVEST_LUMBER) >= 1;
        canLeafBlow = canUseSubskill(mmoPlayer, SubSkillType.WOODCUTTING_LEAF_BLOWER);
        canKnockOnWood = canTreeFell && canUseSubskill(mmoPlayer, SubSkillType.WOODCUTTING_KNOCK_ON_WOOD);
        /*canSplinter = canUseSubskill(player, SubSkillType.WOODCUTTING_SPLINTER);
        canBarkSurgeon = canUseSubskill(player, SubSkillType.WOODCUTTING_BARK_SURGEON);
        canNaturesBounty = canUseSubskill(player, SubSkillType.WOODCUTTING_NATURES_BOUNTY);*/
    }

    @Override
    protected @NotNull List<String> statsDisplay(@NotNull OnlineMMOPlayer mmoPlayer, float skillValue, boolean hasEndurance, boolean isLucky) {
        List<String> messages = new ArrayList<>();

        if (canDoubleDrop) {
            messages.add(getStatMessage(SubSkillType.WOODCUTTING_HARVEST_LUMBER, doubleDropChance)
                    + (isLucky ? LocaleLoader.getString("Perks.Lucky.Bonus", doubleDropChanceLucky) : ""));
        }

        if (canKnockOnWood) {
            String lootNote;

            if(RankUtils.hasReachedRank(2, mmoPlayer, SubSkillType.WOODCUTTING_KNOCK_ON_WOOD)) {
                lootNote = LocaleLoader.getString("Woodcutting.SubSkill.KnockOnWood.Loot.Rank2");
            } else {
                lootNote = LocaleLoader.getString("Woodcutting.SubSkill.KnockOnWood.Loot.Normal");
            }

            messages.add(getStatMessage(SubSkillType.WOODCUTTING_KNOCK_ON_WOOD, lootNote));
        }
        
        if (canLeafBlow) {
            messages.add(LocaleLoader.getString("Ability.Generic.Template", LocaleLoader.getString("Woodcutting.Ability.0"), LocaleLoader.getString("Woodcutting.Ability.1")));
        }

        if (canTreeFell) {
            messages.add(getStatMessage(SubSkillType.WOODCUTTING_TREE_FELLER, treeFellerLength)
                    + (hasEndurance ? LocaleLoader.getString("Perks.ActivationTime.Bonus", treeFellerLengthEndurance) : ""));
        }

        return messages;
    }

    @Override
    protected @NotNull List<Component> getTextComponents(@NotNull OnlineMMOPlayer mmoPlayer) {
        List<Component> textComponents = new ArrayList<>();

        TextComponentFactory.getSubSkillTextComponents(mmoPlayer, textComponents, CoreSkills.WOODCUTTING);

        return textComponents;
    }


}
