package Customs.mods;

public class olyResetSkills
{

	public static final int[] dontResetThoseSkillsAtOly = {3158, 1324};

	public static boolean ResetSkillAtOly(int skillId)
	{
		for (int notResetableSkill : dontResetThoseSkillsAtOly) {
			if (notResetableSkill == skillId)
				return false;
		}
		return true;
	}
	
	
}
