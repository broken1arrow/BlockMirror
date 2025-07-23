package org.brokenarrow.blockmirror.blockpatterns.utily;

import org.brokenarrow.blockmirror.api.blockpattern.PatternSetting;
import org.brokenarrow.blockmirror.api.builders.ItemWrapperApi;
import org.brokenarrow.blockmirror.api.builders.patterns.PatternDisplayItem;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PatternWrapper implements PatternDisplayItem {

	private final ItemWrapperApi passive;
	private final ItemWrapperApi active;
	private final List<PatternSetting> patternSetting = new ArrayList<>();
	private boolean fillAllBlocks;

	public PatternWrapper(final ItemWrapperApi passive, final ItemWrapperApi active) {
		this.passive = passive;
		this.active = active;
	}

	@Override
	public List<PatternSetting> getPatternSettings() {
		return patternSetting;
	}

	@Override
	public PatternWrapper setPatternSettingsWraper(final PatternSetting patternSetting) {
		this.patternSetting.add(patternSetting);
		return this;
	}

	@Override
	public PatternWrapper setPatternSettingsWraper(final PatternSetting... patternSetting) {
		this.patternSetting.addAll(Arrays.asList(patternSetting));
		return this;
	}

	@Override
	@Nullable
	public ItemWrapperApi getItemWrapper(boolean active) {
		if (this.getActive() == null)
			return this.getPassive();
		if (active)
			return this.getActive();
		return this.getPassive();
	}

	@Override
	@Nullable
	public ItemWrapperApi getPassive() {
		return passive;
	}

	@Override
	@Nullable
	public ItemWrapperApi getActive() {
		return active;
	}

	@Override
	public boolean isFillAllBlocks() {
		return fillAllBlocks;
	}

	@Override
	public PatternDisplayItem fillAllBlocks(boolean fillAllBlocks) {
		this.fillAllBlocks = fillAllBlocks;
		return this;
	}
}
