package com.mobiquityinc.mobit.steps.vars.scenario_attrs;

import cucumber.api.java.After;

public final class ScenarioAttributesHooks {

    @After
    public void afterScenario() {
        ScenarioAttribute.resetAll();
    }

}