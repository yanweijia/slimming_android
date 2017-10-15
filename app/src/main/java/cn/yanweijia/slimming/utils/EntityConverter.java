package cn.yanweijia.slimming.utils;

import android.util.Log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.yanweijia.slimming.entity.LocationRecordPoint;
import cn.yanweijia.slimming.entity.RunRecord;
import cn.yanweijia.slimming.entity.RunRecordUpload;

/**
 * Created by weijia on 15/10/2017.
 *
 * @author weijia
 */

public class EntityConverter {
    private static ObjectMapper objectMapper;
    private static final String TAG = "EntityConverter";

    static {
        objectMapper = new ObjectMapper();
    }

    /**
     * convert RunRecord to RunRecordUpload
     *
     * @param runRecord
     * @return
     */
    public static RunRecordUpload convertRunRecordToUploadFormat(RunRecord runRecord) {
        RunRecordUpload runRecordUpload = new RunRecordUpload();
        runRecordUpload.setId(runRecord.getId());
        runRecordUpload.setUserId(runRecord.getUserId());
        runRecordUpload.setStarttime(runRecord.getStarttime());
        runRecordUpload.setEndtime(runRecord.getEndtime());
        runRecordUpload.setDistance(runRecord.getDistance());
        runRecordUpload.setCalorie(runRecord.getCalorie());
        runRecordUpload.setSpeed(runRecord.getSpeed());
        runRecordUpload.setPace(runRecord.getPace());
        runRecordUpload.setRemark(runRecord.getRemark());
        try {
            runRecordUpload.setRoad(objectMapper.writeValueAsString(runRecord.getRoad()));
        } catch (JsonProcessingException e) {
            Log.e(TAG, "convertRunRecordToUploadFormat: ", e);
        }
        return runRecordUpload;
    }

    /**
     * convert RunRecordUpload to RunRecord
     *
     * @param runRecordUpload
     * @return
     */
    public static RunRecord convertRunRecordUploadFormatToNormal(RunRecordUpload runRecordUpload) {
        RunRecord runRecord = new RunRecord();
        runRecord.setId(runRecordUpload.getId());
        runRecord.setUserId(runRecordUpload.getUserId());
        runRecord.setStarttime(runRecordUpload.getStarttime());
        runRecord.setEndtime(runRecordUpload.getEndtime());
        runRecord.setDistance(runRecordUpload.getDistance());
        runRecord.setCalorie(runRecordUpload.getCalorie());
        runRecord.setSpeed(runRecordUpload.getSpeed());
        runRecord.setPace(runRecordUpload.getPace());
        runRecord.setRemark(runRecordUpload.getRemark());


        JavaType javaType = getCollectionType(ArrayList.class, LocationRecordPoint.class);
        List<LocationRecordPoint> list = null;
        try {
            list = objectMapper.readValue(runRecordUpload.getRoad(), javaType);
        } catch (IOException e) {
            Log.e(TAG, "convertRunRecordUploadFormatToNormal: ", e);
        }
        runRecord.setRoad(list);
        return runRecord;
    }

    /**
     * 获取泛型的Collection Type<br/>
     * 参考: <a href="http://www.cnblogs.com/quanyongan/archive/2013/04/16/3024993.html">Jackson将json字符串转换成泛型List</a>
     *
     * @param collectionClass 泛型的Collection
     * @param elementClasses  元素类
     * @return JavaType Java类型
     * @since 1.0
     */
    private static JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
        return objectMapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
    }
}
