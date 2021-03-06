package org.itstack.demo.design.change.service.engine.impl;

import org.itstack.demo.design.change.model.aggregates.TreeRich;
import org.itstack.demo.design.change.model.vo.EngineResult;
import org.itstack.demo.design.change.model.vo.TreeNode;
import org.itstack.demo.design.change.service.engine.EngineBase;

import java.util.Map;

public class TreeEngineHandle extends EngineBase {

    @Override
    public EngineResult process(Long treeId, String userId, TreeRich treeRich, Map<String, String> decisionMatter) {
        // 决策流程
        TreeNode treeNode = engineDecisionMaker(treeRich, treeId, userId, decisionMatter);
        // 决策结果
        return new EngineResult(userId, treeId, treeNode.getTreeNodeId(), treeNode.getNodeValue());
    }

}
