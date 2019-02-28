package springbatch.config

import org.springframework.batch.core.explore.JobExplorer
import org.springframework.batch.core.explore.support.SimpleJobExplorer
import org.springframework.batch.core.launch.support.SimpleJobLauncher
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.repository.support.MapJobRepositoryFactoryBean
import org.springframework.batch.support.transaction.ResourcelessTransactionManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class InMemoryJobRepositoryConfig {
    @Bean
    fun transactionManager(): ResourcelessTransactionManager {
        return ResourcelessTransactionManager()
    }

    @Bean
    @Throws(Exception::class)
    fun mapJobRepositoryFactory(transactionManager: ResourcelessTransactionManager): MapJobRepositoryFactoryBean {
        val factory = MapJobRepositoryFactoryBean(transactionManager)
        factory.afterPropertiesSet()
        return factory
    }

    @Bean
    @Throws(Exception::class)
    fun jobRepository(repositoryFactory: MapJobRepositoryFactoryBean): JobRepository {
        return repositoryFactory.getObject()
    }

    @Bean
    fun jobExplorer(repositoryFactory: MapJobRepositoryFactoryBean): JobExplorer {
        return SimpleJobExplorer(repositoryFactory.jobInstanceDao, repositoryFactory.jobExecutionDao,
                repositoryFactory.stepExecutionDao, repositoryFactory.executionContextDao)
    }

    @Bean
    fun jobLauncher(jobRepository: JobRepository): SimpleJobLauncher {
        val launcher = SimpleJobLauncher()
        launcher.setJobRepository(jobRepository)
        return launcher
    }
}