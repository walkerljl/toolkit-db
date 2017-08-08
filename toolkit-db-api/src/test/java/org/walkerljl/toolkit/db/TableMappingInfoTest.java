package org.walkerljl.toolkit.db;

import org.junit.Assert;
import org.junit.Test;
import org.walkerljl.toolkit.db.api.TableMappingInfo;

import java.util.Map;

/**
 * TableMappingInfoTest
 *
 * @author: lijunlin
 */
public class TableMappingInfoTest {

    @Test
    public void test() {
        TableMappingInfo tableMappingInfo = new TableMappingInfo();

        tableMappingInfo = new TableMappingInfo("id_user", "id",
                new String[]{
                        "id:true->id",
                        "user_id->userId",
                        "user_name->userName",
                        "remark->remark",
                        "status->status",
                "created->created",
                "creator->creator",
                "modified->modified",
                "modifier->modifier"});


        Assert.assertEquals("id_user", tableMappingInfo.getTableName());
        Assert.assertEquals("id", tableMappingInfo.getPrimaryKey());

        Map<String, String> fieldColumnMapping = tableMappingInfo.getFieldColumnMapping();
        Assert.assertNotNull(fieldColumnMapping);
        Assert.assertEquals("id", fieldColumnMapping.get("id"));
        Assert.assertEquals("user_id", fieldColumnMapping.get("userId"));
        Assert.assertEquals("user_name", fieldColumnMapping.get("userName"));
        Assert.assertEquals("remark", fieldColumnMapping.get("remark"));
        Assert.assertEquals("status", fieldColumnMapping.get("status"));
        Assert.assertEquals("created", fieldColumnMapping.get("created"));
        Assert.assertEquals("creator", fieldColumnMapping.get("creator"));
        Assert.assertEquals("modified", fieldColumnMapping.get("modified"));
        Assert.assertEquals("modifier", fieldColumnMapping.get("modifier"));
    }
}
