# Configuring a database

## Local

### PostgreSQL

Running a PostgreSQL instance can easily be done by using `docker compose`:

```bash
$ docker compose up -d
```

> Note: If you start the application locally please be sure that `local` profile is active.


## Kubernetes 

> Integrating with Tanzu Application Platform (TAP)

### PostgreSQL

### Deployment

#### Tanzu Application Platform (TAP)
Using the `config/workload.yaml` it is possible to build, test and deploy this application onto a
Kubernetes cluster that is provisioned with Tanzu Application Platform (https://tanzu.vmware.com/application-platform).

As with the local deployment a PostgreSQL instance needs to be available at the cluster.
When using VMware Tanzu SQL with Postgres for Kubernetes (https://tanzu.vmware.com/sql and https://docs.vmware.com/en/VMware-Tanzu-SQL-with-Postgres-for-Kubernetes/index.html),
one could apply for an instance, and it will be automatically provisioned.

> Note: The Tanzu Postgres Operator must be installed in the cluster.

> Note: please define the storage class to be used for the PostgreSQL storage.

```bash
$ kubectl apply -n <your-services-namespace> -f config/service-operator/postgres.yaml
```

When the PostgreSQL instance is created, resource binding needs to be configured in order that your workload can access
the PostgreSQL instance, which maybe be in another namespace than your application workload.

1. If the `ClusterRole` and `ClusterInstanceClass` for service binding has not been defined, then as a service operator, you can run the following command.

   ```bash
   $ kubectl apply -f config/service-operator/postgres-cluster-role.yaml
   $ kubectl apply -f config/service-operator/postgres-cluster-instance-class.yaml
   ```

2. Configure the `ResourceClaimPolicy` to define which namespaces could have access to your PostgreSQL instance namespaces.
   (optional: only needed when PostgreSQL instance is in different namespace than workload namespace).

   Edit `consumingNamespaces` in `config/app-operator/postgres-resource-claim-policy.yaml` to contain your workload namespace and apply:
   
   ```bash
   $ kubectl apply -n <your-services-namespace> -f config/app-operator/postgres-resource-claim-policy.yaml
   ```

3. Create the `ResourceClaim` to be consumed by your workload that references your PostgreSQL instance:
   > Note: change the `spec.ref.namespace` of `config/app-operator/postgres-resource-claim.yaml` to where the PostgreSQL instance is deployed.
   
   ```bash
   $ kubectl apply -n <your-workload-namespace> -f config/app-operator/postgres-resource-claim.yaml
   ```

Now that the resource binding is configured the `Workload` can be applied.

