package conspire.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;

import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

// Several patches to places where card.upgrade() and card.canUpgrade() are called

// Actions
@SpirePatch(clz=com.megacrit.cardcrawl.actions.unique.ApotheosisAction.class, method="upgradeAllCardsInGroup")
@SpirePatch(clz=com.megacrit.cardcrawl.actions.unique.ArmamentsAction.class, method="update")
@SpirePatch(clz=com.megacrit.cardcrawl.actions.unique.ExhumeAction.class, method="update")
// Note: SacrificeAction and TransmutationAction only upgrade unupgraded cards

// Core logic
@SpirePatch(clz=com.megacrit.cardcrawl.cards.AbstractCard.class, method="renderInLibrary")
@SpirePatch(clz=com.megacrit.cardcrawl.cards.AbstractCard.class, method="makeStatEquivalentCopy")
// Note: AbstractCard.renderCardTip only cares about rawDescription
// Note: AbstractDungeon.getRewardCards() only upgrades fresh rewards
@SpirePatch(clz=com.megacrit.cardcrawl.cards.CardGroup.class, method="getUpgradableCards")
@SpirePatch(clz=com.megacrit.cardcrawl.cards.CardGroup.class, method="hasUpgradableCards")
@SpirePatch(clz=com.megacrit.cardcrawl.helpers.CardLibrary.class, method="getCopy", paramtypez={java.lang.String.class,int.class,int.class})
@SpirePatch(clz=com.megacrit.cardcrawl.screens.runHistory.RunHistoryScreen.class, method="cardForName")
@SpirePatch(clz=com.megacrit.cardcrawl.screens.select.GridCardSelectScreen.class, method="update")
@SpirePatch(clz=com.megacrit.cardcrawl.screens.select.HandCardSelectScreen.class, method="selectHoveredCard")
@SpirePatch(clz=com.megacrit.cardcrawl.screens.select.HandCardSelectScreen.class, method="updateMessage")
@SpirePatch(clz=com.megacrit.cardcrawl.screens.SingleCardViewPopup.class, method="render")
@SpirePatch(clz=com.megacrit.cardcrawl.vfx.campfire.CampfireSmithEffect.class, method="update")
// Note: AbstractCard.makeStatEquivalentCopy() already copies stats, except for magicNumber

// Events
@SpirePatch(clz=com.megacrit.cardcrawl.events.shrines.AccursedBlacksmith.class, method="update")
@SpirePatch(clz=com.megacrit.cardcrawl.events.shrines.Designer.class, method="update")
@SpirePatch(clz=com.megacrit.cardcrawl.events.shrines.Designer.class, method="upgradeTwoRandomCards")
@SpirePatch(clz=com.megacrit.cardcrawl.events.shrines.UpgradeShrine.class, method="update")
@SpirePatch(clz=com.megacrit.cardcrawl.events.exordium.LivingWall.class, method="update")
@SpirePatch(clz=com.megacrit.cardcrawl.events.exordium.ShiningLight.class, method="upgradeCards")
@SpirePatch(clz=com.megacrit.cardcrawl.events.city.BackToBasics.class, method="upgradeStrikeAndDefends")
// Note: NeowReward is at start of game

// Relics
// Note: Don't care about eggs
@SpirePatch(clz=com.megacrit.cardcrawl.relics.TinyHouse.class, method="onEquip")
@SpirePatch(clz=com.megacrit.cardcrawl.relics.WarPaint.class, method="onEquip")
@SpirePatch(clz=com.megacrit.cardcrawl.relics.Whetstone.class, method="onEquip")

// BaseMod patches
@SpirePatch(clz=basemod.DevConsole.class, method="cmdHand")
@SpirePatch(clz=basemod.DevConsole.class, method="cmdDeck")
@SpirePatch(clz=basemod.patches.com.megacrit.cardcrawl.events.BackToBasics.UpgradeStrikeAndDefends.class, method="Insert")
@SpirePatch(clz=basemod.patches.com.megacrit.cardcrawl.events.NoteForYourself.MissingCard.class, method="Replace")

// MadScienceMod patches
@SpirePatch(cls="madsciencemod.actions.common.HandToTopOfDrawPileAction", method="update", optional=true)
@SpirePatch(cls="madsciencemod.actions.unique.ProbeAction", method="update", optional=true)
@SpirePatch(cls="madsciencemod.actions.unique.TinkerAction", method="update", optional=true)
@SpirePatch(cls="madsciencemod.cards.Weaponized", method="upgrade", optional=true)

// ReplayTheSpire patches
@SpirePatch(cls="com.megacrit.cardcrawl.cards.red.Hemogenesis", method="tookDamage", optional=true)
@SpirePatch(cls="com.megacrit.cardcrawl.ui.campfire.BonfireMultitaskOption", method="useOption", optional=true)
@SpirePatch(cls="replayTheSpire.patches.ReplayCampFirePatch", method="Postfix", optional=true)
@SpirePatch(cls="replayTheSpire.patches.MakeTempCardPatches.ReplayMakeCardInDrawPilePatch", method="Prefix", optional=true)
@SpirePatch(cls="replayTheSpire.patches.MakeTempCardPatches.ReplayMakeCardInDiscardPilePatch", method="Prefix", optional=true)

// Hubris patches
@SpirePatch(cls="com.evacipated.cardcrawl.mod.hubris.cards.DuctTapeCard", method="canUpgrade", optional=true)
@SpirePatch(cls="com.evacipated.cardcrawl.mod.hubris.cards.DuctTapeCard", method="upgrade", optional=true)


public class InfiniteJournalUpgradePatch {

    public static ExprEditor patch = new ExprEditor() {
        public void edit(MethodCall m) throws CannotCompileException {
            // replace  c.canUpgrade() by InfiniteJournal.canUpgradeCard(c)
            // replace: c.upgrade()    by InfiniteJournal.upgradeCard(c)
            if (m.getClassName().equals("com.megacrit.cardcrawl.cards.AbstractCard")) {
                if (m.getMethodName().equals("canUpgrade")) {
                    m.replace("$_ = conspire.relics.InfiniteJournal.canUpgradeCard($0);");
                } else if (m.getMethodName().equals("upgrade")) {
                    m.replace("conspire.relics.InfiniteJournal.upgradeCard($0);");
                }
            }
        }
    };

    public static ExprEditor Instrument() {
        return patch;
    }
}
