package AppTest;

import common.java.security.codec;

public class TestWeb2 {
    public static void main(String[] args) {
//    	String encodeFastJSON = codec.encodeFastJSON("{\"ownid\":\"1\",\"name\":\"美女局\",\"fatherid\":0,\"sort\":4,\"wbgid\":\"B80DFC0D9BBF7E2A8C1CC6209F4D1BD6\"}");
//    	String encodeFastJSON = codec.encodeFastJSON("{\"_id\":\"5ad55a1e8682852740df9003\"}");
//    	String encodeFastJSON = codec.encodeFastJSON("{\"_id\":\"5ad55a1e8682852740df9003\",\"_ChildrenData\":\"\",\"dMode\":0,\"dValue\":500,\"isvisble\":0,\"sort\":4,\"wbgid\":\"B80DFC0D9BBF7E2A8C1CC6209F4D1BD6\",\"uValue\":50,\"rValue\":0,\"uMode\":0,\"powerVal\":0,\"ownid\":\"1\",\"visable\":0,\"deleteable\":0,\"itemfatherID \":\"0\",\"name\":\"美女局1\",\"fatherid\":0,\"_FatherID\":\"\",\"isdelete\":0,\"itemSort\":0,\"_Level\":0,\"rMode\":0,\"itemLevel\":0,\"_Sort\":0}");
    	String encodeFastJSON = codec.encodeFastJSON("{\"itemfatherID\":\"0\",\"dMode\":null,\"engerid\":\"0\",\"wbgid\":\"B80DFC0D9BBF7E2A8C1CC6209F4D1BD6\",\"type\":\"0\",\"title\":\"李琼测试1\",\"uMode\":null,\"tid\":\"0\",\"gov\":0,\"desp\":\"11111\",\"ownid\":\"\",\"linkId\":\"0\",\"visable\":0,\"deleteable\":0,\"icp\":\"0\",\"fatherid\":\"0\",\"host\":\"\",\"logo\":\"\",\"itemSort\":0,\"rMode\":null,\"itemLevel\":0}");
    	
    	System.out.println(encodeFastJSON);
    }
}
