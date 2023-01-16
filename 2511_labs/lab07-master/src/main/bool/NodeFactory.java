package bool;

import org.json.JSONArray;
import org.json.JSONObject;

public class NodeFactory {
    public static BooleanNode factoryBuild(JSONObject Node) {
        JSONObject subNode1;
        JSONObject subNode2;
        switch(Node.getString("node")) {
            case "and":
            AndComposite andComposite = new AndComposite();
            subNode1 = Node.getJSONObject("subnode1");
            subNode2 = Node.getJSONObject("subnode2");
            andComposite.add(factoryBuild(subNode1));
            andComposite.add(factoryBuild(subNode2));
            return andComposite;

            case "or":
            OrComposite orComposite = new OrComposite();
            subNode1 = Node.getJSONObject("subnode1");
            subNode2 = Node.getJSONObject("subnode2");
            orComposite.add(factoryBuild(subNode1));
            orComposite.add(factoryBuild(subNode2));
            return orComposite;

            case "not":
            NotComposite notComposite = new NotComposite();
            subNode1 = Node.getJSONObject("subnode1");
            notComposite.add(factoryBuild(subNode1));
            return notComposite;
            case "value":
                return Node.getBoolean("value") ? new Leaf(true) : new Leaf(false);
        }
        return null;
    }

}