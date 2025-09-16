# Outputs for Dormventory Infrastructure

output "vpc_id" {
  description = "ID of the VPC"
  value       = aws_vpc.dormventory_vpc.id
}

output "vpc_cidr_block" {
  description = "CIDR block of the VPC"
  value       = aws_vpc.dormventory_vpc.cidr_block
}

output "public_subnet_ids" {
  description = "IDs of the public subnets"
  value       = aws_subnet.public_subnets[*].id
}

output "private_subnet_ids" {
  description = "IDs of the private subnets"
  value       = aws_subnet.private_subnets[*].id
}

output "database_endpoint" {
  description = "RDS instance endpoint"
  value       = aws_db_instance.dormventory_db.endpoint
  sensitive   = true
}

output "database_port" {
  description = "RDS instance port"
  value       = aws_db_instance.dormventory_db.port
}

output "s3_bucket_name" {
  description = "Name of the S3 bucket"
  value       = aws_s3_bucket.dormventory_bucket.bucket
}

output "s3_bucket_arn" {
  description = "ARN of the S3 bucket"
  value       = aws_s3_bucket.dormventory_bucket.arn
}

output "ecr_repository_url" {
  description = "URL of the ECR repository"
  value       = aws_ecr_repository.dormventory_repo.repository_url
}

output "ecs_cluster_name" {
  description = "Name of the ECS cluster"
  value       = aws_ecs_cluster.dormventory_cluster.name
}

output "ecs_cluster_arn" {
  description = "ARN of the ECS cluster"
  value       = aws_ecs_cluster.dormventory_cluster.arn
}

output "alb_dns_name" {
  description = "DNS name of the Application Load Balancer"
  value       = aws_lb.dormventory_alb.dns_name
}

output "alb_zone_id" {
  description = "Zone ID of the Application Load Balancer"
  value       = aws_lb.dormventory_alb.zone_id
}

output "alb_arn" {
  description = "ARN of the Application Load Balancer"
  value       = aws_lb.dormventory_alb.arn
}

output "cloudwatch_log_group_name" {
  description = "Name of the CloudWatch log group"
  value       = aws_cloudwatch_log_group.dormventory_logs.name
}

output "cloudwatch_log_group_arn" {
  description = "ARN of the CloudWatch log group"
  value       = aws_cloudwatch_log_group.dormventory_logs.arn
}

output "secrets_manager_secrets" {
  description = "ARNs of the Secrets Manager secrets"
  value = {
    db_password    = aws_secretsmanager_secret.db_password.arn
    aws_access_key = aws_secretsmanager_secret.aws_access_key.arn
    aws_secret_key = aws_secretsmanager_secret.aws_secret_key.arn
    jwt_secret     = aws_secretsmanager_secret.jwt_secret.arn
  }
  sensitive = true
}

output "application_url" {
  description = "URL to access the application"
  value       = "http://${aws_lb.dormventory_alb.dns_name}"
}

output "environment_variables" {
  description = "Environment variables for the application"
  value = {
    AWS_REGION     = var.aws_region
    AWS_S3_BUCKET  = aws_s3_bucket.dormventory_bucket.bucket
    DB_HOST        = aws_db_instance.dormventory_db.endpoint
    DB_PORT        = aws_db_instance.dormventory_db.port
    DB_NAME        = "dormventory"
    DB_USERNAME    = var.db_username
    ECR_REPOSITORY = aws_ecr_repository.dormventory_repo.repository_url
  }
  sensitive = false
}

output "deployment_commands" {
  description = "Commands to deploy the application"
  value = {
    build_and_push = "aws ecr get-login-password --region ${var.aws_region} | docker login --username AWS --password-stdin ${aws_ecr_repository.dormventory_repo.repository_url} && docker build -t ${aws_ecr_repository.dormventory_repo.name} . && docker tag ${aws_ecr_repository.dormventory_repo.name}:latest ${aws_ecr_repository.dormventory_repo.repository_url}:latest && docker push ${aws_ecr_repository.dormventory_repo.repository_url}:latest"
    update_service = "aws ecs update-service --cluster ${aws_ecs_cluster.dormventory_cluster.name} --service ${aws_ecs_service.dormventory_service.name} --force-new-deployment --region ${var.aws_region}"
  }
}
