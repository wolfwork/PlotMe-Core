package com.worldcretornica.configuration.file;

import com.worldcretornica.configuration.ConfigurationSection;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.representer.Representer;

class YamlRepresenter extends Representer {

    public YamlRepresenter() {
        this.multiRepresenters.put(ConfigurationSection.class, new RepresentConfigurationSection());
    }

    private class RepresentConfigurationSection extends RepresentMap {

        @Override
        public Node representData(Object data) {
            return super.representData(((ConfigurationSection) data).getValues(false));
        }
    }
}
