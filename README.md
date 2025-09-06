# HzCompactSerialization
* Serializers will created for custom types. Hazelcast compact serialization provide serializer as default for  primitive types and specific type (String, Date etc.). For instance you have a complex type (Student) class and the class has a complex type (Lecture) field, how can you serialize these types? We will try to find the reply this question.

*Compact Serialization provides two approaches: zero-config and custom compact. Zero-config requires no configuration, while custom compact must be registered explicitly in Hazelcast, using different methods. I am going to explain.
