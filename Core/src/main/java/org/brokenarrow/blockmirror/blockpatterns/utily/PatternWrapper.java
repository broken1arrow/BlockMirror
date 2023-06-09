package org.brokenarrow.blockmirror.blockpatterns.utily;

import org.brokenarrow.blockmirror.api.blockpattern.PatternSettingsWrapperApi;
import org.brokenarrow.blockmirror.api.builders.ItemWrapper;
import org.brokenarrow.blockmirror.api.builders.patterns.PatternWrapperApi;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PatternWrapper implements PatternWrapperApi {

	private final ItemWrapper passive;
	private final ItemWrapper active;
	private List<PatternSettingsWrapperApi> patternSettingsWrapperApi = new ArrayList<>();
	private boolean fillBlocks;

	public PatternWrapper(final ItemWrapper passive, final ItemWrapper active) {
		this.passive = passive;
		this.active = active;
	}

	@Override
	public boolean isFillBlocks() {
		return fillBlocks;
	}

	@Override
	public PatternWrapper setFillBlocks(final boolean fillBlocks) {
		this.fillBlocks = fillBlocks;
		return this;
	}

	@Override
	public List<PatternSettingsWrapperApi> getPatternSettingsWrapperApi() {
		return patternSettingsWrapperApi;
	}

	@Override
	public PatternWrapper setPatternSettingsWraperApi(final PatternSettingsWrapperApi patternSettingsWrapperApi) {
		this.patternSettingsWrapperApi.add(patternSettingsWrapperApi);
		return this;
	}

	@Override
	public PatternWrapper setPatternSettingsWraperApi(final PatternSettingsWrapperApi... patternSettingsWrapperApi) {
		this.patternSettingsWrapperApi.addAll(Arrays.asList(patternSettingsWrapperApi));
		return this;
	}

	@Override
	@Nullable
	public ItemWrapper getItemWrapper(boolean active) {
		if (this.getActive() == null)
			return this.getPassive();
		if (active)
			return this.getActive();
		return this.getPassive();
	}

	@Override
	@Nullable
	public ItemWrapper getPassive() {
		return passive;
	}

	@Override
	@Nullable
	public ItemWrapper getActive() {
		return active;
	}
}
