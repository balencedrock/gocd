/*
 * Copyright 2017 ThoughtWorks, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.thoughtworks.go.plugin.access.authorization;

import com.thoughtworks.go.plugin.domain.authorization.AuthorizationPluginInfo;
import com.thoughtworks.go.plugin.domain.authorization.Capabilities;
import com.thoughtworks.go.plugin.domain.authorization.SupportedAuthType;
import com.thoughtworks.go.plugin.domain.common.Image;
import com.thoughtworks.go.plugin.domain.common.PluggableInstanceSettings;
import com.thoughtworks.go.plugin.domain.common.PluginConfiguration;
import com.thoughtworks.go.plugin.domain.common.PluginView;
import com.thoughtworks.go.plugin.infra.plugininfo.GoPluginDescriptor;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AuthorizationPluginInfoBuilderTest {

    private AuthorizationExtension extension;

    @Before
    public void setUp() throws Exception {
        extension = mock(AuthorizationExtension.class);
    }

    @Test
    public void shouldBuildPluginInfoWithAuthSettings() throws Exception {
        GoPluginDescriptor descriptor =  new GoPluginDescriptor("plugin1", null, null, null, null, false);
        List<PluginConfiguration> pluginConfigurations = Arrays.asList(new PluginConfiguration("username", Collections.singletonMap("secure", false)));

        when(extension.getAuthConfigMetadata(descriptor.id())).thenReturn(pluginConfigurations);
        when(extension.getAuthConfigView(descriptor.id())).thenReturn("auth_config");

        AuthorizationPluginInfo pluginInfo = new AuthorizationPluginInfoBuilder(extension).pluginInfoFor(descriptor);

        assertThat(pluginInfo.getAuthConfigSettings(), is(new PluggableInstanceSettings(pluginConfigurations, new PluginView("auth_config"))));
    }

    @Test
    public void shouldBuildPluginInfoWithRoleSettings() throws Exception {
        GoPluginDescriptor descriptor =  new GoPluginDescriptor("plugin1", null, null, null, null, false);
        List<PluginConfiguration> pluginConfigurations = Arrays.asList(new PluginConfiguration("group", Collections.singletonMap("secure", false)));

        when(extension.getRoleConfigurationMetadata(descriptor.id())).thenReturn(pluginConfigurations);
        when(extension.getRoleConfigurationView(descriptor.id())).thenReturn("role_config");

        AuthorizationPluginInfo pluginInfo = new AuthorizationPluginInfoBuilder(extension).pluginInfoFor(descriptor);

        assertThat(pluginInfo.getRoleSettings(), is(new PluggableInstanceSettings(pluginConfigurations, new PluginView("role_config"))));
    }

    @Test
    public void shouldBuildPluginInfoWithPluginDescriptor() throws Exception {
        GoPluginDescriptor descriptor =  new GoPluginDescriptor("plugin1", null, null, null, null, false);

        AuthorizationPluginInfo pluginInfo = new AuthorizationPluginInfoBuilder(extension).pluginInfoFor(descriptor);

        assertThat(pluginInfo.getDescriptor(), is(descriptor));
    }

    @Test
    public void shouldBuildPluginInfoWithImage() throws Exception {
        GoPluginDescriptor descriptor =  new GoPluginDescriptor("plugin1", null, null, null, null, false);
        Image icon = new Image("content_type", "data");

        when(extension.getIcon(descriptor.id())).thenReturn(icon);

        AuthorizationPluginInfo pluginInfo = new AuthorizationPluginInfoBuilder(extension).pluginInfoFor(descriptor);

        assertThat(pluginInfo.getImage(), is(icon));
    }

    @Test
    public void shouldBuildPluginInfoWithCapablities() throws Exception {
        GoPluginDescriptor descriptor =  new GoPluginDescriptor("plugin1", null, null, null, null, false);
        Capabilities capabilities = new Capabilities(SupportedAuthType.Password, true);

        when(extension.getCapabilities(descriptor.id())).thenReturn(capabilities);

        AuthorizationPluginInfo pluginInfo = new AuthorizationPluginInfoBuilder(extension).pluginInfoFor(descriptor);

        assertThat(pluginInfo.getCapabilities(), is(capabilities));
    }
}
