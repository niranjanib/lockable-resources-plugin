/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Copyright (c) 2013, Aki Asikainen. All rights reserved.             *
 *                                                                     *
 * This file is part of the Jenkins Lockable Resources Plugin and is   *
 * published under the MIT license.                                    *
 *                                                                     *
 * See the "LICENSE.txt" file for more information.                    *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package org.jenkins.plugins.lockableresources.queue;

import hudson.EnvVars;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.jenkins.plugins.lockableresources.LockableResource;
import org.jenkins.plugins.lockableresources.LockableResourcesManager;
import org.jenkins.plugins.lockableresources.RequiredResourcesProperty;
import org.jenkinsci.plugins.scriptsecurity.sandbox.groovy.SecureGroovyScript;

public class LockableResourcesStruct implements Serializable {

	public List<LockableResource> required;
	public String label;
	public String requiredVar;
	public String requiredNumber;
	public SecureGroovyScript script;

	public LockableResourcesStruct(RequiredResourcesProperty property,
			EnvVars env) {
		required = new ArrayList<LockableResource>();
		for (String name : property.getResources()) {
			LockableResource r = LockableResourcesManager.get().fromName(
				env.expand(name));
			if (r != null) {
				this.required.add(r);
			}
		}

		label = env.expand(property.getLabelName());
		if (label == null)
			label = "";

		script = property.getScript();

		requiredVar = property.getResourceNamesVar();

		requiredNumber = property.getResourceNumber();
		if (requiredNumber != null && requiredNumber.equals("0"))
			requiredNumber = null;
	}

	public LockableResourcesStruct(String resource) {
		required = new ArrayList<LockableResource>();
		LockableResource r = LockableResourcesManager.get().fromName(resource);
		if (r != null) {
			this.required.add(r);
		}
	}

	public String toString() {
		return "Required resources: " + this.required +
			", Required label: " + this.label +
			", Required label script: " + (this.script != null ? this.script.getScript() : "") +
			", Variable name: " + this.requiredVar +
			", Number of resources: " + this.requiredNumber;
	}

	private static final long serialVersionUID = 1L;
}
