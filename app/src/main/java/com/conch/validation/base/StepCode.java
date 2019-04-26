package com.conch.validation.base;

/**
 * 流程
 */
public interface StepCode {
    /**
     * 准备阶段
     */
    int READY = 0;

    /**
     * 第一步:基本信息输入
     */
    int FIRST = 1;

    /**
     * 第二步:身份证识别比对
     */
    int SECOND = 2;

    /**
     * 第三步:人脸检测
     */
    int THIRD = 3;
}
