package com.baguix.web.service.cms.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.baguix.utils.db.QueryConditionFilter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baguix.web.dao.BaseDaoI;
import com.baguix.web.model.db.cms.TAnnouncement;
import com.baguix.web.model.easyui.DataGrid;
import com.baguix.web.model.page.cms.Article;
import com.baguix.web.service.cms.AnnouncementServiceI;
import com.baguix.web.service.impl.BaseServiceImpl;

@Service("announcementService")
public class AnnouncementServiceImpl extends BaseServiceImpl<TAnnouncement> implements AnnouncementServiceI {
	
	private BaseDaoI<TAnnouncement> dao;
	
	public BaseDaoI<TAnnouncement> getDao() {
		return dao;
	}
	
	@Autowired
	public void setDao(BaseDaoI<TAnnouncement> dao) {
		this.dao = dao;
	}
	
	@Override
	synchronized public Article add(Article art) {
		TAnnouncement t = new TAnnouncement();
		BeanUtils.copyProperties(art, t);
		t.setId(UUID.randomUUID().toString());
		t.setCtime(new Date());
		t.setMtime(new Date());
		dao.save(t);
		BeanUtils.copyProperties(t, art);
		art.setContent(null);
		art.setAbstracts(null);
		return art;
	}
	
	@Override
	public void delete(String ids) {
		String[] nids = ids.split(",");
		String hql = "update TAnnouncement t set t.isdel=true where t.id in (";
		for (int i = 0; i < nids.length; i++) {
			if (i > 0) {
				hql += ",";
			}
			hql += "'" + nids[i] + "'";
		}
		hql += ")";
		dao.executeHql(hql);
	}
	
	@Override
	public void remove(String ids) {
		String[] nids = ids.split(",");
		String hql = "delete TAnnouncement t where t.id in (";
		for (int i = 0; i < nids.length; i++) {
			if (i > 0) {
				hql += ",";
			}
			hql += "'" + nids[i] + "'";
		}
		hql += ")";
		dao.executeHql(hql);
	}

	@Override
	synchronized public Article edit(Article art) {
		TAnnouncement t = new TAnnouncement();
		BeanUtils.copyProperties(art, t);
		t.setMtime(new Date());
		dao.saveOrUpdate(t);
		BeanUtils.copyProperties(t, art);
		art.setContent(null);
		art.setAbstracts(null);
		return art;
	}

	@Override
	public Article view(Article art) {
		TAnnouncement t = dao.getById(TAnnouncement.class, art.getId());
		BeanUtils.copyProperties(t, art);
		return art;
	}
	
	@Override
	public Article view(String id) {
		Article art = new Article();
		TAnnouncement t = dao.getById(TAnnouncement.class, id);
		BeanUtils.copyProperties(t, art);
		return art;
	}
	
	@Override
	public DataGrid datagrid(Article art) {
		DataGrid dg = new DataGrid();
		String hql = "from TAnnouncement t where 1=1 ";
		Map<String, Object> params = new HashMap<String, Object>();
		hql = addWhere(art, hql, params);
		String totalHql = "select count(*) " + hql;
		hql = addOrder(art, hql);
		List<TAnnouncement> l = dao.find(hql, params, art.getPage(), art.getRows());
		List<Article> nl = new ArrayList<Article>();
		changeModel(l, nl);
		dg.setTotal(dao.count(totalHql, params));
		dg.setRows(nl);
		return dg;
	}
	
	@Override
	public DataGrid search(Article art) {
		if(art.getQcondition()==null){
			return datagrid(art);
		}
		else if(StringUtils.isEmpty(art.getQcondition())){
			return datagrid(art);
		}
		else{
			DataGrid dg = new DataGrid();
			String hql = "from TAnnouncement t where 1=1 ";
			Map<String, Object> params = new HashMap<String, Object>();
			if(art.getQcondition()!= null){
				QueryConditionFilter qcf = new QueryConditionFilter();
				qcf.addFilter(art.getQcondition());
				hql += qcf.getWhereHql();
				params = qcf.getParams();
				if (art.getSort() != null) {
					hql += " order by t." + art.getSort() + " " + art.getOrder() + ", "+ qcf.getOrderHql();
				}
				else{
					hql += " order by " + qcf.getOrderHql();
				}
			}
			String totalHql = "select count(*) " + hql;
			List<TAnnouncement> l = dao.find(hql, params, art.getPage(), art.getRows());
			List<Article> nl = new ArrayList<Article>();
			changeModel(l, nl);
			dg.setTotal(dao.count(totalHql, params));
			dg.setRows(nl);
			return dg;
		}
		
	}
	
	//private
	private void changeModel(List<TAnnouncement> l, List<Article> nl) {
		if (l != null && l.size() > 0) {
			for (TAnnouncement t : l) {
				Article u = new Article();
				BeanUtils.copyProperties(t, u);
				nl.add(u);
			}
		}
	}

	private String addOrder(Article art, String hql) {
		if (art.getSort() != null) {
			hql += " order by " + art.getSort() + " " + art.getOrder();
		}
		return hql;
	}

	private String addWhere(Article art, String hql, Map<String, Object> params) {
		if ( StringUtils.isNotEmpty( art.getTitle()) ) {
			hql += " where where t.title like :title";
			params.put("title", "%%" + art.getTitle().trim() + "%%");
		}
		return hql;
	}
	
}
