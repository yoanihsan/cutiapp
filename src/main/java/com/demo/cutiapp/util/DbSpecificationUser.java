package com.demo.cutiapp.util;

import lombok.*;
import org.springframework.data.jpa.domain.Specification;

import com.demo.cutiapp.model.Employee;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@AllArgsConstructor @NoArgsConstructor @Getter @Setter @ToString
public class DbSpecificationUser implements Specification<Employee> {

	/**
	 *
	 */
	private static final long serialVersionUID = -7987210387779152223L;
	private SearchCriteria criteria;
	private Predicate predicate;

	@Override
	public Predicate toPredicate(Root<Employee> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
		if (criteria.getOperation().equalsIgnoreCase(">")) {
			return builder.and(builder.equal(root.get("deleted"), false),
					builder.greaterThanOrEqualTo(root.get(criteria.getKey()), criteria.getValue().toString()));
		}
		else if (criteria.getOperation().equalsIgnoreCase("<")) {
			return builder.and(builder.equal(root.get("deleted"), false), builder.lessThanOrEqualTo(
					root.<String> get(criteria.getKey()), criteria.getValue().toString()));
		}
		else if (criteria.getOperation().equalsIgnoreCase(":")) {
			if((criteria.getKey().equalsIgnoreCase("lastUserId") && criteria.getValue() == null) || criteria.getKey().equalsIgnoreCase("")) {criteria.setKey("userName");}
			if((criteria.getKey().equalsIgnoreCase("roleName"))) {criteria.setKey("userName");}
			if (root.get(criteria.getKey()).getJavaType() == String.class) {
                return builder.and(builder.equal(root.get("deleted"), false), builder.like(builder.lower(root.<String>get(criteria.getKey())), "%" + criteria.getValue().toString().toLowerCase() + "%"));
			} else {
				return builder.and(builder.equal(root.get("deleted"), false), builder.equal(root.get(criteria.getKey()), criteria.getValue()));
			}
		}
		else if (criteria.getOperation().equalsIgnoreCase("=")) {
			return builder.and(builder.equal(root.get("deleted"), false), builder.equal(root.get(criteria.getKey()), criteria.getValue()));
		}

		return null;
	}
}