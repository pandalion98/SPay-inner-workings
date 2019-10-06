package com.goodix.cap.fingerprint;

public class GFErrorCode {
    private static final String UNKOWN_ERROR_MESSAGE = "错误";

    private enum Error {
        GF_SUCCESS("成功", 0),
        GF_ERROR_BASE(GFErrorCode.UNKOWN_ERROR_MESSAGE, 1000),
        GF_ERROR_OUT_OF_MEMORY("内存溢出", 1001),
        GF_ERROR_OPEN_TA_FAILED("打开TA失败", 1002),
        GF_ERROR_BAD_PARAMS("参数错误", 1003),
        GF_ERROR_NO_SPACE("空间不足", 1004),
        GF_ERROR_REACH_FINGERS_UPLIMIT("指纹已满", 1005),
        GF_ERROR_NOT_MATCH("匹配失败", 1006),
        GF_ERROR_CANCELED("已取消", 1007),
        GF_ERROR_TIMEOUT("超时", 1008),
        GF_ERROR_PREPROCESS_FAILED("预处理失败", 1009),
        GF_ERROR_GENERIC(GFErrorCode.UNKOWN_ERROR_MESSAGE, 1010),
        GF_ERROR_ACQUIRED_PARTIAL("有效面积过少", 1011),
        GF_ERROR_ACQUIRED_IMAGER_DIRTY("图像质量太差", 1012),
        GF_ERROR_DUPLICATE_FINGER("重复手指", 1013),
        GF_ERROR_OPEN_DEVICE_FAILED("打开设备失败", 1014),
        GF_ERROR_HAL_GENERAL_ERROR("hal层错误", 1015),
        GF_ERROR_HAL_FILE_DESCRIPTION_NULL("文件描述空", 1016),
        GF_ERROR_HAL_IOCTL_FAILED("ioctl调用失败", 1017),
        GF_ERROR_HAL_TIMER_FUNC(GFErrorCode.UNKOWN_ERROR_MESSAGE, 1018),
        GF_ERROR_CORRUPT_CONTENT("文件内容损坏", 1019),
        GF_ERROR_INCORRECT_VERSION("错误的版本", 1020),
        GF_ERROR_CORRUPT_OBJECT(GFErrorCode.UNKOWN_ERROR_MESSAGE, 1021),
        GF_ERROR_INVALID_DATA("无效数据", 1022),
        GF_ERROR_SPI_TRANSFER_ERROR("SPI传输错误", 1023),
        GF_ERROR_SPI_GENERAL_ERROR("SPI错误", 1024),
        GF_ERROR_SPI_IRQ_HANDLE("中断处理错误", 1025),
        GF_ERROR_SPI_RAW_DATA_CRC_FAILED("SPI数据crc错误", 1026),
        GF_ERROR_SPI_RAW_DATA_BUF_BUSY(GFErrorCode.UNKOWN_ERROR_MESSAGE, 1027),
        GF_ERROR_SPI_FW_CFG_DATA_ERROR("固件配置文件错误", 1028),
        GF_ERROR_SPI_FW_DOWNLOAD_FAILED("下固件失败", 1029),
        GF_ERROR_SPI_CFG_DOWNLOAD_FAILED("下配置失败", 1030),
        GF_ERROR_SAVE_FP_TEMPLATE("保存模版失败", 1031),
        GF_ERROR_FP_BUSY(GFErrorCode.UNKOWN_ERROR_MESSAGE, 1032),
        GF_ERROR_OPEN_SECURE_OBJECT_FAILED("打开安全文件失败", 1033),
        GF_ERROR_READ_SECURE_OBJECT_FAILED("读取安全文件失败", 1034),
        GF_ERROR_WRITE_SECURE_OBJECT_FAILED("写安全文件失败", 1035),
        GF_ERROR_SECURE_OBJECT_NOT_EXIST("安全文件不存在", 1036),
        GF_ERROR_WRITE_CONFIG_FAILED("写配置失败", 1039),
        GF_ERROR_TEST_SENSOR_FAILED(GFErrorCode.UNKOWN_ERROR_MESSAGE, 1040),
        GF_ERROR_SET_MODE_FAILED("设模式失败", 1041),
        GF_ERROR_CHIP_ID_NOT_CORRECT("chip ID错误", 1042),
        GF_ERROR_MAX_NUM(GFErrorCode.UNKOWN_ERROR_MESSAGE, 1043),
        GF_ERROR_TEST_BAD_POINT_FAILED("坏点测试失败", 1044),
        GF_ERROR_TEST_FRR_FAR_ENROLL_DIFFERENT_FINGER("...", 1045),
        GF_ERROR_DUPLICATE_AREA("重叠率过高", 1046),
        GF_ERROR_SPI_COMMUNICATION("SPI通信失败", 1047),
        GF_ERROR_FINGER_NOT_EXIST("指纹不存在", 1048),
        GF_ERROR_INVALID_PREPROCESS_VERSION("错误的预处理版本", 1049),
        GF_ERROR_TA_DEAD("TA crash", 1050),
        GF_ERROR_NAV_TOO_FAST("手指移动太快", 1051),
        GF_ERROR_UNSUPPORT_CHIP("不支持的芯片类型", 1052),
        GF_ERROR_INVALID_FINGER_PRESS(GFErrorCode.UNKOWN_ERROR_MESSAGE, 1053),
        GF_ERROR_TA_GENERATE_RANDOM(GFErrorCode.UNKOWN_ERROR_MESSAGE, 1054),
        GF_ERROR_BIO_ASSAY_FAIL("活体认证失败", 1055),
        GF_ERROR_INVALID_HAT_VERSION(GFErrorCode.UNKOWN_ERROR_MESSAGE, 1056),
        GF_ERROR_INVALID_CHALLENGE(GFErrorCode.UNKOWN_ERROR_MESSAGE, 1057),
        GF_ERROR_UNTRUSTED_ENROLL("不受信任的注册", 1058),
        GF_ERROR_INVALID_BASE("无效的基帧", 1059),
        GF_ERROR_SENSOR_BROKEN_CHECK_NEXT_FRAME(GFErrorCode.UNKOWN_ERROR_MESSAGE, 1060),
        GF_ERROR_SENSOR_BROKEN_CHECK_ALGO_ERROR(GFErrorCode.UNKOWN_ERROR_MESSAGE, 1061),
        GF_ERROR_SENSOR_IS_BROKEN("芯片损坏", 1062),
        GF_ERROR_SENSOR_NOT_AVAILABLE("芯片不可用", 1063),
        GF_ERROR_SENSOR_TEST_FAILED("芯片测试失败", 1064),
        GF_ERROR_NATIVE_SERVICE_BASE("native服务错误", 2000),
        GF_ERROR_NATIVE_SERVICE_GETSERVICE("native服务获取失败", 2001),
        GF_ERROR_NATIVE_SERVICE_INIT_HAL("native服务初始化hal层失败", 2002),
        GF_ERROR_MAX(GFErrorCode.UNKOWN_ERROR_MESSAGE, 3000);
        
        /* access modifiers changed from: private */
        public int mErrorCode;
        /* access modifiers changed from: private */
        public String mMessage;

        private Error(String name, int code) {
            this.mMessage = name;
            this.mErrorCode = code;
        }
    }

    public static String getErrorMessage(int errorCode) {
        Error[] values;
        for (Error e : Error.values()) {
            if (e.mErrorCode == errorCode) {
                return e.mMessage;
            }
        }
        return null;
    }
}
