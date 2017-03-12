package android.media.audiopolicy;

import android.media.AudioAttributes;
import android.os.Parcel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

public class AudioMixingRule {
    public static final int RULE_EXCLUDE_ATTRIBUTE_CAPTURE_PRESET = 32770;
    public static final int RULE_EXCLUDE_ATTRIBUTE_USAGE = 32769;
    private static final int RULE_EXCLUSION_MASK = 32768;
    public static final int RULE_MATCH_ATTRIBUTE_CAPTURE_PRESET = 2;
    public static final int RULE_MATCH_ATTRIBUTE_USAGE = 1;
    private final ArrayList<AttributeMatchCriterion> mCriteria;
    private final int mTargetMixType;

    static final class AttributeMatchCriterion {
        AudioAttributes mAttr;
        int mRule;

        AttributeMatchCriterion(AudioAttributes attributes, int rule) {
            this.mAttr = attributes;
            this.mRule = rule;
        }

        public int hashCode() {
            return Objects.hash(new Object[]{this.mAttr, Integer.valueOf(this.mRule)});
        }

        void writeToParcel(Parcel dest) {
            dest.writeInt(this.mRule);
            if (this.mRule == 1 || this.mRule == 32769) {
                dest.writeInt(this.mAttr.getUsage());
            } else {
                dest.writeInt(this.mAttr.getCapturePreset());
            }
        }
    }

    public static class Builder {
        private ArrayList<AttributeMatchCriterion> mCriteria = new ArrayList();
        private int mTargetMixType = -1;

        public Builder addRule(AudioAttributes attrToMatch, int rule) throws IllegalArgumentException {
            if (AudioMixingRule.isValidSystemApiRule(rule)) {
                return addRuleInt(attrToMatch, rule);
            }
            throw new IllegalArgumentException("Illegal rule value " + rule);
        }

        public Builder excludeRule(AudioAttributes attrToMatch, int rule) throws IllegalArgumentException {
            if (AudioMixingRule.isValidSystemApiRule(rule)) {
                return addRuleInt(attrToMatch, 32768 | rule);
            }
            throw new IllegalArgumentException("Illegal rule value " + rule);
        }

        Builder addRuleInt(AudioAttributes attrToMatch, int rule) throws IllegalArgumentException {
            if (attrToMatch == null) {
                throw new IllegalArgumentException("Illegal null AudioAttributes argument");
            } else if (AudioMixingRule.isValidIntRule(rule)) {
                if (this.mTargetMixType == -1) {
                    if (AudioMixingRule.isPlayerRule(rule)) {
                        this.mTargetMixType = 0;
                    } else {
                        this.mTargetMixType = 1;
                    }
                } else if ((this.mTargetMixType == 0 && !AudioMixingRule.isPlayerRule(rule)) || (this.mTargetMixType == 1 && AudioMixingRule.isPlayerRule(rule))) {
                    throw new IllegalArgumentException("Incompatible rule for mix");
                }
                synchronized (this.mCriteria) {
                    Iterator<AttributeMatchCriterion> crIterator = this.mCriteria.iterator();
                    while (crIterator.hasNext()) {
                        AttributeMatchCriterion criterion = (AttributeMatchCriterion) crIterator.next();
                        if (rule == 1 || rule == 32769) {
                            if (criterion.mAttr.getUsage() == attrToMatch.getUsage()) {
                                if (criterion.mRule == rule) {
                                } else {
                                    throw new IllegalArgumentException("Contradictory rule exists for " + attrToMatch);
                                }
                            }
                        } else if (rule == 2 || rule == 32770) {
                            if (criterion.mAttr.getCapturePreset() == attrToMatch.getCapturePreset()) {
                                if (criterion.mRule == rule) {
                                } else {
                                    throw new IllegalArgumentException("Contradictory rule exists for " + attrToMatch);
                                }
                            }
                        }
                    }
                    this.mCriteria.add(new AttributeMatchCriterion(attrToMatch, rule));
                }
                return this;
            } else {
                throw new IllegalArgumentException("Illegal rule value " + rule);
            }
        }

        Builder addRuleFromParcel(Parcel in) throws IllegalArgumentException {
            AudioAttributes attr;
            int rule = in.readInt();
            if (rule == 1 || rule == 32769) {
                attr = new android.media.AudioAttributes.Builder().setUsage(in.readInt()).build();
            } else if (rule == 2 || rule == 32770) {
                attr = new android.media.AudioAttributes.Builder().setInternalCapturePreset(in.readInt()).build();
            } else {
                in.readInt();
                throw new IllegalArgumentException("Illegal rule value " + rule + " in parcel");
            }
            return addRuleInt(attr, rule);
        }

        public AudioMixingRule build() {
            return new AudioMixingRule(this.mTargetMixType, this.mCriteria);
        }
    }

    private AudioMixingRule(int mixType, ArrayList<AttributeMatchCriterion> criteria) {
        this.mCriteria = criteria;
        this.mTargetMixType = mixType;
    }

    int getTargetMixType() {
        return this.mTargetMixType;
    }

    ArrayList<AttributeMatchCriterion> getCriteria() {
        return this.mCriteria;
    }

    public int hashCode() {
        return Objects.hash(new Object[]{Integer.valueOf(this.mTargetMixType), this.mCriteria});
    }

    private static boolean isValidSystemApiRule(int rule) {
        switch (rule) {
            case 1:
            case 2:
                return true;
            default:
                return false;
        }
    }

    private static boolean isValidIntRule(int rule) {
        switch (rule) {
            case 1:
            case 2:
            case 32769:
            case 32770:
                return true;
            default:
                return false;
        }
    }

    private static boolean isPlayerRule(int rule) {
        return rule == 1 || rule == 32769;
    }
}
