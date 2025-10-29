// package br.com.gubee.interview.core.features.factories;

// import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
// import org.springframework.stereotype.Service;

// import br.com.gubee.interview.core.domain.services.HeroRowMapper;
// import br.com.gubee.interview.core.features.hero.HeroRepository;
// import br.com.gubee.interview.core.features.interfaces.IHeroRepository;
// import lombok.RequiredArgsConstructor;

// @Deprecated
// @Service
// @RequiredArgsConstructor
// public class HeroRepositoryFactory {

//     private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
//     private final HeroRowMapper heroRowMapper;

//     public IHeroRepository create(){
//         return new HeroRepository(heroRowMapper, namedParameterJdbcTemplate);
//     }
// }
