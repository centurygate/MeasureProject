package measurebussiness.dao;

import measurebussiness.model.Measure;
import measurebussiness.model.MeasureKey;

import java.math.BigInteger;
import java.util.List;

public interface MeasureMapper
{
    int deleteByPrimaryKey(MeasureKey key);
    
    int insert(Measure record);
    
    int insertSelective(Measure record);
    
    Measure selectByPrimaryKey(MeasureKey key);
    
    int updateByPrimaryKeySelective(Measure record);
    
    int updateByPrimaryKey(Measure record);
    
    List<Measure> queryMeasureByAddrTime(String devAddr, Long startTime,Long endTime);
}