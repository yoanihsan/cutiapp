package com.demo.cutiapp.util;

import lombok.*;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@AllArgsConstructor @NoArgsConstructor @Getter @Setter @ToString
public class DbSpecification<T> implements Specification<T> {

	private static final long serialVersionUID = -7987210387779152223L;
	//	private SearchCriteria criteria;
	private SearchCriteria criteria;
	private Predicate predicate;

	@Override
	public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
		if (criteria.getOperation().equalsIgnoreCase(">")) {
			return builder.and(builder.equal(root.get("deleted"), false),
					builder.greaterThanOrEqualTo(root.get(criteria.getKey()), criteria.getValue().toString()));
		}
		else if (criteria.getOperation().equalsIgnoreCase("<")) {
			return builder.and(builder.equal(root.get("deleted"), false), builder.lessThanOrEqualTo(
					root.<String> get(criteria.getKey()), criteria.getValue().toString()));
		}
		else if (criteria.getOperation().equalsIgnoreCase(":")) {
			if(criteria.getKey().equalsIgnoreCase("")) {criteria.setKey("name");}
			if (root.get(criteria.getKey()).getJavaType() == String.class) {
                return builder.and(builder.equal(root.get("deleted"), false), builder.like(builder.lower(root.<String>get(criteria.getKey())), "%" + criteria.getValue().toString().toLowerCase() + "%"));
			} else {
				return builder.and(builder.equal(root.get("deleted"), false), builder.equal(root.get(criteria.getKey()), criteria.getValue()));
			}
		}
		else if (criteria.getOperation().equalsIgnoreCase("*")) {
			return builder.equal(root.get("deleted"), false);
		}
		
		return null;
	}
}

