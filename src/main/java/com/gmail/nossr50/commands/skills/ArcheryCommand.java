package com.gmail.nossr50.commands.skills;

import com.gmail.nossr50.datatypes.skills.SubSkillType;
import com.gmail.nossr50.locale.LocaleLoader;
import com.gmail.nossr50.skills.archery.Archery;
import com.gmail.nossr50.util.skills.CombatUtils;
import com.gmail.nossr50.util.skills.SkillActivationType;
import com.gmail.nossr50.util.text.TextComponentFactory;
import com.neetgames.mcmmo.player.OnlineMMOPlayer;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ArcheryCommand extends SkillCommand {
    private String skillShotBonus;
    private String dazeChance;
    private String dazeChanceLucky;
    private String retrieveChance;
    private String retrieveChanceLucky;

    private boolean canSkillShot;
    private boolean canDaze;
    private boolean canRetrieve;

    public ArcheryCommand() {
        super(CoreSkills.ARCHERY);
    }

    @Override
    protected void dataCalculations(@NotNull OnlineMMOPlayer mmoPlayer, float skillValue) {
        // ARCHERY_ARROW_RETRIEVAL
        if (canRetrieve) {
            String[] retrieveStrings = getAbilityDisplayValues(SkillActivationType.RANDOM_LINEAR_100_SCALE_WITH_CAP, mmoPlayer, SubSkillType.ARCHERY_ARROW_RETRIEVAL);
            retrieveChance = retrieveStrings[0];
            retrieveChanceLucky = retrieveStrings[1];
        }
        
        // ARCHERY_DAZE
        if (canDaze) {
            String[] dazeStrings = getAbilityDisplayValues(SkillActivationType.RANDOM_LINEAR_100_SCALE_WITH_CAP, mmoPlayer, SubSkillType.ARCHERY_DAZE);
            dazeChance = dazeStrings[0];
            dazeChanceLucky = dazeStrings[1];
        }
        
        // rootSkillSHOT
        if (canSkillShot) {
            skillShotBonus = percent.format(Archery.getDamageBonusPercent(mmoPlayer));
        }
    }

    @Override
    protected void permissionsCheck(@NotNull OnlineMMOPlayer mmoPlayer) {
        canSkillShot = canUseSubskill(mmoPlayer, SubSkillType.ARCHERY_SKILL_SHOT);
        canDaze = canUseSubskill(mmoPlayer, SubSkillType.ARCHERY_DAZE);
        canRetrieve = canUseSubskill(mmoPlayer, SubSkillType.ARCHERY_ARROW_RETRIEVAL);
    }

    @Override
    protected @NotNull List<String> statsDisplay(@NotNull OnlineMMOPlayer mmoPlayer, float skillValue, boolean hasEndurance, boolean isLucky) {
        List<String> messages = new ArrayList<>();

        if (canRetrieve) {
            messages.add(getStatMessage(SubSkillType.ARCHERY_ARROW_RETRIEVAL, retrieveChance)
                    + (isLucky ? LocaleLoader.getString("Perks.Lucky.Bonus", retrieveChanceLucky) : ""));
        }
        
        if (canDaze) {
            messages.add(getStatMessage(SubSkillType.ARCHERY_DAZE, dazeChance)
                    + (isLucky ? LocaleLoader.getString("Perks.Lucky.Bonus", dazeChanceLucky) : ""));
        }
        
        if (canSkillShot) {
            messages.add(getStatMessage(SubSkillType.ARCHERY_SKILL_SHOT, skillShotBonus));
        }

        if(canUseSubskill(mmoPlayer, SubSkillType.ARCHERY_ARCHERY_LIMIT_BREAK)) {
            messages.add(getStatMessage(SubSkillType.ARCHERY_ARCHERY_LIMIT_BREAK,
                String.valueOf(CombatUtils.getLimitBreakDamageAgainstQuality(mmoPlayer, SubSkillType.ARCHERY_ARCHERY_LIMIT_BREAK, 1000))));
        }

        return messages;
    }

    @Override
    protected @NotNull List<Component> getTextComponents(@NotNull OnlineMMOPlayer mmoPlayer) {
        List<Component> textComponents = new ArrayList<>();

        TextComponentFactory.getSubSkillTextComponents(mmoPlayer, textComponents, CoreSkills.ARCHERY);

        return textComponents;
    }
}
