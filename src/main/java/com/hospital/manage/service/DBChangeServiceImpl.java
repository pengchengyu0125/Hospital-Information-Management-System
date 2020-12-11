package com.hospital.manage.service;

import com.hospital.manage.config.DBContextHolder;
import com.hospital.manage.config.DynamicDataSource;
import com.hospital.manage.mapper.DataSourceMapper;
import com.hospital.manage.model.DataSource;
import com.hospital.manage.tools.DesEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/***
 * 切换数据库
 */
@Service
public class DBChangeServiceImpl implements DBChangeService {

    @Autowired
    DataSourceMapper dataSourceMapper;
    @Autowired
    private DynamicDataSource dynamicDataSource;

    @Override
    public List<DataSource> get() {
        return dataSourceMapper.get();
    }

    /***
     * 切换数据库
     * @param datasourceId 医院编码
     * @return
     * @throws Exception
     */
    @Override
    public boolean changeDb(String datasourceId) throws Exception {

        //默认切换到主数据源,进行整体资源的查找
        DBContextHolder.clearDataSource();

        List<DataSource> dataSourcesList = dataSourceMapper.get();

        //解密数据库信息
        DesEncoder encoder = new DesEncoder();
        for (int i = 0; i < dataSourcesList.size(); i++) {
            dataSourcesList.get(i).setUrl(encoder.decrypt(dataSourcesList.get(i).getUrl(), dataSourcesList.get(i).getHospitalName()));
            dataSourcesList.get(i).setPassword(encoder.decrypt(dataSourcesList.get(i).getPassword(), dataSourcesList.get(i).getIconUrl()));
        }

        for (DataSource dataSource : dataSourcesList) {
            if (dataSource.getHospitalCode().equals(datasourceId)) {
                System.out.println("需要使用的的数据源已经找到,datasourceId是：" + dataSource.getHospitalCode());
                //创建数据源连接&检查 若存在则不需重新创建
                dynamicDataSource.createDataSourceWithCheck(dataSource);
                //切换到该数据源
                DBContextHolder.setDataSource(dataSource.getHospitalCode());
                return true;
            }
        }
        return false;

    }

}
