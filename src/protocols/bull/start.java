// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: bull/start.proto

package protocols.bull;

public final class start {
  private start() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  public interface requestOrBuilder extends
      // @@protoc_insertion_point(interface_extends:request)
      com.google.protobuf.MessageOrBuilder {
  }
  /**
   * <pre>
   * 请求
   * </pre>
   *
   * Protobuf type {@code request}
   */
  public  static final class request extends
      com.google.protobuf.GeneratedMessageV3 implements
      // @@protoc_insertion_point(message_implements:request)
      requestOrBuilder {
    // Use request.newBuilder() to construct.
    private request(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
      super(builder);
    }
    private request() {
    }

    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
    getUnknownFields() {
      return com.google.protobuf.UnknownFieldSet.getDefaultInstance();
    }
    private request(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      this();
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              break;
            default: {
              if (!input.skipField(tag)) {
                done = true;
              }
              break;
            }
          }
        }
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(
            e).setUnfinishedMessage(this);
      } finally {
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return protocols.bull.start.internal_static_request_descriptor;
    }

    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return protocols.bull.start.internal_static_request_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              protocols.bull.start.request.class, protocols.bull.start.request.Builder.class);
    }

    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized == 1) return true;
      if (isInitialized == 0) return false;

      memoizedIsInitialized = 1;
      return true;
    }

    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
    }

    public int getSerializedSize() {
      int size = memoizedSize;
      if (size != -1) return size;

      size = 0;
      memoizedSize = size;
      return size;
    }

    private static final long serialVersionUID = 0L;
    @java.lang.Override
    public boolean equals(final java.lang.Object obj) {
      if (obj == this) {
       return true;
      }
      if (!(obj instanceof protocols.bull.start.request)) {
        return super.equals(obj);
      }
      protocols.bull.start.request other = (protocols.bull.start.request) obj;

      boolean result = true;
      return result;
    }

    @java.lang.Override
    public int hashCode() {
      if (memoizedHashCode != 0) {
        return memoizedHashCode;
      }
      int hash = 41;
      hash = (19 * hash) + getDescriptor().hashCode();
      hash = (29 * hash) + unknownFields.hashCode();
      memoizedHashCode = hash;
      return hash;
    }

    public static protocols.bull.start.request parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static protocols.bull.start.request parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static protocols.bull.start.request parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static protocols.bull.start.request parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static protocols.bull.start.request parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static protocols.bull.start.request parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }
    public static protocols.bull.start.request parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input);
    }
    public static protocols.bull.start.request parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
    }
    public static protocols.bull.start.request parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static protocols.bull.start.request parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }

    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder() {
      return DEFAULT_INSTANCE.toBuilder();
    }
    public static Builder newBuilder(protocols.bull.start.request prototype) {
      return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() {
      return this == DEFAULT_INSTANCE
          ? new Builder() : new Builder().mergeFrom(this);
    }

    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    /**
     * <pre>
     * 请求
     * </pre>
     *
     * Protobuf type {@code request}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:request)
        protocols.bull.start.requestOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return protocols.bull.start.internal_static_request_descriptor;
      }

      protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return protocols.bull.start.internal_static_request_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                protocols.bull.start.request.class, protocols.bull.start.request.Builder.class);
      }

      // Construct using protocols.bull.start.request.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
          com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessageV3
                .alwaysUseFieldBuilders) {
        }
      }
      public Builder clear() {
        super.clear();
        return this;
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return protocols.bull.start.internal_static_request_descriptor;
      }

      public protocols.bull.start.request getDefaultInstanceForType() {
        return protocols.bull.start.request.getDefaultInstance();
      }

      public protocols.bull.start.request build() {
        protocols.bull.start.request result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public protocols.bull.start.request buildPartial() {
        protocols.bull.start.request result = new protocols.bull.start.request(this);
        onBuilt();
        return result;
      }

      public Builder clone() {
        return (Builder) super.clone();
      }
      public Builder setField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          Object value) {
        return (Builder) super.setField(field, value);
      }
      public Builder clearField(
          com.google.protobuf.Descriptors.FieldDescriptor field) {
        return (Builder) super.clearField(field);
      }
      public Builder clearOneof(
          com.google.protobuf.Descriptors.OneofDescriptor oneof) {
        return (Builder) super.clearOneof(oneof);
      }
      public Builder setRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          int index, Object value) {
        return (Builder) super.setRepeatedField(field, index, value);
      }
      public Builder addRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          Object value) {
        return (Builder) super.addRepeatedField(field, value);
      }
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof protocols.bull.start.request) {
          return mergeFrom((protocols.bull.start.request)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(protocols.bull.start.request other) {
        if (other == protocols.bull.start.request.getDefaultInstance()) return this;
        onChanged();
        return this;
      }

      public final boolean isInitialized() {
        return true;
      }

      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        protocols.bull.start.request parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (protocols.bull.start.request) e.getUnfinishedMessage();
          throw e.unwrapIOException();
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }
      public final Builder setUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return this;
      }

      public final Builder mergeUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return this;
      }


      // @@protoc_insertion_point(builder_scope:request)
    }

    // @@protoc_insertion_point(class_scope:request)
    private static final protocols.bull.start.request DEFAULT_INSTANCE;
    static {
      DEFAULT_INSTANCE = new protocols.bull.start.request();
    }

    public static protocols.bull.start.request getDefaultInstance() {
      return DEFAULT_INSTANCE;
    }

    private static final com.google.protobuf.Parser<request>
        PARSER = new com.google.protobuf.AbstractParser<request>() {
      public request parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
          return new request(input, extensionRegistry);
      }
    };

    public static com.google.protobuf.Parser<request> parser() {
      return PARSER;
    }

    @java.lang.Override
    public com.google.protobuf.Parser<request> getParserForType() {
      return PARSER;
    }

    public protocols.bull.start.request getDefaultInstanceForType() {
      return DEFAULT_INSTANCE;
    }

  }

  public interface responseOrBuilder extends
      // @@protoc_insertion_point(interface_extends:response)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <pre>
     * 错误代码，0=成功 其他=错误
     * </pre>
     *
     * <code>sint32 error = 1;</code>
     */
    int getError();

    /**
     * <pre>
     * 庄家座位ID
     * </pre>
     *
     * <code>uint32 dealer = 2;</code>
     */
    int getDealer();

    /**
     * <pre>
     * 局数
     * </pre>
     *
     * <code>uint32 inning = 3;</code>
     */
    int getInning();

    /**
     * <pre>
     * 错误描述
     * </pre>
     *
     * <code>string errDesc = 4;</code>
     */
    java.lang.String getErrDesc();
    /**
     * <pre>
     * 错误描述
     * </pre>
     *
     * <code>string errDesc = 4;</code>
     */
    com.google.protobuf.ByteString
        getErrDescBytes();
  }
  /**
   * <pre>
   * 返回
   * </pre>
   *
   * Protobuf type {@code response}
   */
  public  static final class response extends
      com.google.protobuf.GeneratedMessageV3 implements
      // @@protoc_insertion_point(message_implements:response)
      responseOrBuilder {
    // Use response.newBuilder() to construct.
    private response(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
      super(builder);
    }
    private response() {
      error_ = 0;
      dealer_ = 0;
      inning_ = 0;
      errDesc_ = "";
    }

    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
    getUnknownFields() {
      return com.google.protobuf.UnknownFieldSet.getDefaultInstance();
    }
    private response(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      this();
      int mutable_bitField0_ = 0;
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              break;
            default: {
              if (!input.skipField(tag)) {
                done = true;
              }
              break;
            }
            case 8: {

              error_ = input.readSInt32();
              break;
            }
            case 16: {

              dealer_ = input.readUInt32();
              break;
            }
            case 24: {

              inning_ = input.readUInt32();
              break;
            }
            case 34: {
              java.lang.String s = input.readStringRequireUtf8();

              errDesc_ = s;
              break;
            }
          }
        }
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(
            e).setUnfinishedMessage(this);
      } finally {
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return protocols.bull.start.internal_static_response_descriptor;
    }

    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return protocols.bull.start.internal_static_response_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              protocols.bull.start.response.class, protocols.bull.start.response.Builder.class);
    }

    public static final int ERROR_FIELD_NUMBER = 1;
    private int error_;
    /**
     * <pre>
     * 错误代码，0=成功 其他=错误
     * </pre>
     *
     * <code>sint32 error = 1;</code>
     */
    public int getError() {
      return error_;
    }

    public static final int DEALER_FIELD_NUMBER = 2;
    private int dealer_;
    /**
     * <pre>
     * 庄家座位ID
     * </pre>
     *
     * <code>uint32 dealer = 2;</code>
     */
    public int getDealer() {
      return dealer_;
    }

    public static final int INNING_FIELD_NUMBER = 3;
    private int inning_;
    /**
     * <pre>
     * 局数
     * </pre>
     *
     * <code>uint32 inning = 3;</code>
     */
    public int getInning() {
      return inning_;
    }

    public static final int ERRDESC_FIELD_NUMBER = 4;
    private volatile java.lang.Object errDesc_;
    /**
     * <pre>
     * 错误描述
     * </pre>
     *
     * <code>string errDesc = 4;</code>
     */
    public java.lang.String getErrDesc() {
      java.lang.Object ref = errDesc_;
      if (ref instanceof java.lang.String) {
        return (java.lang.String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        errDesc_ = s;
        return s;
      }
    }
    /**
     * <pre>
     * 错误描述
     * </pre>
     *
     * <code>string errDesc = 4;</code>
     */
    public com.google.protobuf.ByteString
        getErrDescBytes() {
      java.lang.Object ref = errDesc_;
      if (ref instanceof java.lang.String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        errDesc_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized == 1) return true;
      if (isInitialized == 0) return false;

      memoizedIsInitialized = 1;
      return true;
    }

    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      if (error_ != 0) {
        output.writeSInt32(1, error_);
      }
      if (dealer_ != 0) {
        output.writeUInt32(2, dealer_);
      }
      if (inning_ != 0) {
        output.writeUInt32(3, inning_);
      }
      if (!getErrDescBytes().isEmpty()) {
        com.google.protobuf.GeneratedMessageV3.writeString(output, 4, errDesc_);
      }
    }

    public int getSerializedSize() {
      int size = memoizedSize;
      if (size != -1) return size;

      size = 0;
      if (error_ != 0) {
        size += com.google.protobuf.CodedOutputStream
          .computeSInt32Size(1, error_);
      }
      if (dealer_ != 0) {
        size += com.google.protobuf.CodedOutputStream
          .computeUInt32Size(2, dealer_);
      }
      if (inning_ != 0) {
        size += com.google.protobuf.CodedOutputStream
          .computeUInt32Size(3, inning_);
      }
      if (!getErrDescBytes().isEmpty()) {
        size += com.google.protobuf.GeneratedMessageV3.computeStringSize(4, errDesc_);
      }
      memoizedSize = size;
      return size;
    }

    private static final long serialVersionUID = 0L;
    @java.lang.Override
    public boolean equals(final java.lang.Object obj) {
      if (obj == this) {
       return true;
      }
      if (!(obj instanceof protocols.bull.start.response)) {
        return super.equals(obj);
      }
      protocols.bull.start.response other = (protocols.bull.start.response) obj;

      boolean result = true;
      result = result && (getError()
          == other.getError());
      result = result && (getDealer()
          == other.getDealer());
      result = result && (getInning()
          == other.getInning());
      result = result && getErrDesc()
          .equals(other.getErrDesc());
      return result;
    }

    @java.lang.Override
    public int hashCode() {
      if (memoizedHashCode != 0) {
        return memoizedHashCode;
      }
      int hash = 41;
      hash = (19 * hash) + getDescriptor().hashCode();
      hash = (37 * hash) + ERROR_FIELD_NUMBER;
      hash = (53 * hash) + getError();
      hash = (37 * hash) + DEALER_FIELD_NUMBER;
      hash = (53 * hash) + getDealer();
      hash = (37 * hash) + INNING_FIELD_NUMBER;
      hash = (53 * hash) + getInning();
      hash = (37 * hash) + ERRDESC_FIELD_NUMBER;
      hash = (53 * hash) + getErrDesc().hashCode();
      hash = (29 * hash) + unknownFields.hashCode();
      memoizedHashCode = hash;
      return hash;
    }

    public static protocols.bull.start.response parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static protocols.bull.start.response parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static protocols.bull.start.response parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static protocols.bull.start.response parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static protocols.bull.start.response parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static protocols.bull.start.response parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }
    public static protocols.bull.start.response parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input);
    }
    public static protocols.bull.start.response parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
    }
    public static protocols.bull.start.response parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static protocols.bull.start.response parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }

    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder() {
      return DEFAULT_INSTANCE.toBuilder();
    }
    public static Builder newBuilder(protocols.bull.start.response prototype) {
      return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() {
      return this == DEFAULT_INSTANCE
          ? new Builder() : new Builder().mergeFrom(this);
    }

    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    /**
     * <pre>
     * 返回
     * </pre>
     *
     * Protobuf type {@code response}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:response)
        protocols.bull.start.responseOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return protocols.bull.start.internal_static_response_descriptor;
      }

      protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return protocols.bull.start.internal_static_response_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                protocols.bull.start.response.class, protocols.bull.start.response.Builder.class);
      }

      // Construct using protocols.bull.start.response.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
          com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessageV3
                .alwaysUseFieldBuilders) {
        }
      }
      public Builder clear() {
        super.clear();
        error_ = 0;

        dealer_ = 0;

        inning_ = 0;

        errDesc_ = "";

        return this;
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return protocols.bull.start.internal_static_response_descriptor;
      }

      public protocols.bull.start.response getDefaultInstanceForType() {
        return protocols.bull.start.response.getDefaultInstance();
      }

      public protocols.bull.start.response build() {
        protocols.bull.start.response result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public protocols.bull.start.response buildPartial() {
        protocols.bull.start.response result = new protocols.bull.start.response(this);
        result.error_ = error_;
        result.dealer_ = dealer_;
        result.inning_ = inning_;
        result.errDesc_ = errDesc_;
        onBuilt();
        return result;
      }

      public Builder clone() {
        return (Builder) super.clone();
      }
      public Builder setField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          Object value) {
        return (Builder) super.setField(field, value);
      }
      public Builder clearField(
          com.google.protobuf.Descriptors.FieldDescriptor field) {
        return (Builder) super.clearField(field);
      }
      public Builder clearOneof(
          com.google.protobuf.Descriptors.OneofDescriptor oneof) {
        return (Builder) super.clearOneof(oneof);
      }
      public Builder setRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          int index, Object value) {
        return (Builder) super.setRepeatedField(field, index, value);
      }
      public Builder addRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          Object value) {
        return (Builder) super.addRepeatedField(field, value);
      }
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof protocols.bull.start.response) {
          return mergeFrom((protocols.bull.start.response)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(protocols.bull.start.response other) {
        if (other == protocols.bull.start.response.getDefaultInstance()) return this;
        if (other.getError() != 0) {
          setError(other.getError());
        }
        if (other.getDealer() != 0) {
          setDealer(other.getDealer());
        }
        if (other.getInning() != 0) {
          setInning(other.getInning());
        }
        if (!other.getErrDesc().isEmpty()) {
          errDesc_ = other.errDesc_;
          onChanged();
        }
        onChanged();
        return this;
      }

      public final boolean isInitialized() {
        return true;
      }

      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        protocols.bull.start.response parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (protocols.bull.start.response) e.getUnfinishedMessage();
          throw e.unwrapIOException();
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }

      private int error_ ;
      /**
       * <pre>
       * 错误代码，0=成功 其他=错误
       * </pre>
       *
       * <code>sint32 error = 1;</code>
       */
      public int getError() {
        return error_;
      }
      /**
       * <pre>
       * 错误代码，0=成功 其他=错误
       * </pre>
       *
       * <code>sint32 error = 1;</code>
       */
      public Builder setError(int value) {
        
        error_ = value;
        onChanged();
        return this;
      }
      /**
       * <pre>
       * 错误代码，0=成功 其他=错误
       * </pre>
       *
       * <code>sint32 error = 1;</code>
       */
      public Builder clearError() {
        
        error_ = 0;
        onChanged();
        return this;
      }

      private int dealer_ ;
      /**
       * <pre>
       * 庄家座位ID
       * </pre>
       *
       * <code>uint32 dealer = 2;</code>
       */
      public int getDealer() {
        return dealer_;
      }
      /**
       * <pre>
       * 庄家座位ID
       * </pre>
       *
       * <code>uint32 dealer = 2;</code>
       */
      public Builder setDealer(int value) {
        
        dealer_ = value;
        onChanged();
        return this;
      }
      /**
       * <pre>
       * 庄家座位ID
       * </pre>
       *
       * <code>uint32 dealer = 2;</code>
       */
      public Builder clearDealer() {
        
        dealer_ = 0;
        onChanged();
        return this;
      }

      private int inning_ ;
      /**
       * <pre>
       * 局数
       * </pre>
       *
       * <code>uint32 inning = 3;</code>
       */
      public int getInning() {
        return inning_;
      }
      /**
       * <pre>
       * 局数
       * </pre>
       *
       * <code>uint32 inning = 3;</code>
       */
      public Builder setInning(int value) {
        
        inning_ = value;
        onChanged();
        return this;
      }
      /**
       * <pre>
       * 局数
       * </pre>
       *
       * <code>uint32 inning = 3;</code>
       */
      public Builder clearInning() {
        
        inning_ = 0;
        onChanged();
        return this;
      }

      private java.lang.Object errDesc_ = "";
      /**
       * <pre>
       * 错误描述
       * </pre>
       *
       * <code>string errDesc = 4;</code>
       */
      public java.lang.String getErrDesc() {
        java.lang.Object ref = errDesc_;
        if (!(ref instanceof java.lang.String)) {
          com.google.protobuf.ByteString bs =
              (com.google.protobuf.ByteString) ref;
          java.lang.String s = bs.toStringUtf8();
          errDesc_ = s;
          return s;
        } else {
          return (java.lang.String) ref;
        }
      }
      /**
       * <pre>
       * 错误描述
       * </pre>
       *
       * <code>string errDesc = 4;</code>
       */
      public com.google.protobuf.ByteString
          getErrDescBytes() {
        java.lang.Object ref = errDesc_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (java.lang.String) ref);
          errDesc_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <pre>
       * 错误描述
       * </pre>
       *
       * <code>string errDesc = 4;</code>
       */
      public Builder setErrDesc(
          java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  
        errDesc_ = value;
        onChanged();
        return this;
      }
      /**
       * <pre>
       * 错误描述
       * </pre>
       *
       * <code>string errDesc = 4;</code>
       */
      public Builder clearErrDesc() {
        
        errDesc_ = getDefaultInstance().getErrDesc();
        onChanged();
        return this;
      }
      /**
       * <pre>
       * 错误描述
       * </pre>
       *
       * <code>string errDesc = 4;</code>
       */
      public Builder setErrDescBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
        
        errDesc_ = value;
        onChanged();
        return this;
      }
      public final Builder setUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return this;
      }

      public final Builder mergeUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return this;
      }


      // @@protoc_insertion_point(builder_scope:response)
    }

    // @@protoc_insertion_point(class_scope:response)
    private static final protocols.bull.start.response DEFAULT_INSTANCE;
    static {
      DEFAULT_INSTANCE = new protocols.bull.start.response();
    }

    public static protocols.bull.start.response getDefaultInstance() {
      return DEFAULT_INSTANCE;
    }

    private static final com.google.protobuf.Parser<response>
        PARSER = new com.google.protobuf.AbstractParser<response>() {
      public response parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
          return new response(input, extensionRegistry);
      }
    };

    public static com.google.protobuf.Parser<response> parser() {
      return PARSER;
    }

    @java.lang.Override
    public com.google.protobuf.Parser<response> getParserForType() {
      return PARSER;
    }

    public protocols.bull.start.response getDefaultInstanceForType() {
      return DEFAULT_INSTANCE;
    }

  }

  private static final com.google.protobuf.Descriptors.Descriptor
    internal_static_request_descriptor;
  private static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_request_fieldAccessorTable;
  private static final com.google.protobuf.Descriptors.Descriptor
    internal_static_response_descriptor;
  private static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_response_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\020bull/start.proto\"\t\n\007request\"J\n\010respons" +
      "e\022\r\n\005error\030\001 \001(\021\022\016\n\006dealer\030\002 \001(\r\022\016\n\006inni" +
      "ng\030\003 \001(\r\022\017\n\007errDesc\030\004 \001(\tB\027\n\016protocols.b" +
      "ullB\005startb\006proto3"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
        new com.google.protobuf.Descriptors.FileDescriptor.    InternalDescriptorAssigner() {
          public com.google.protobuf.ExtensionRegistry assignDescriptors(
              com.google.protobuf.Descriptors.FileDescriptor root) {
            descriptor = root;
            return null;
          }
        };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        }, assigner);
    internal_static_request_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_request_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_request_descriptor,
        new java.lang.String[] { });
    internal_static_response_descriptor =
      getDescriptor().getMessageTypes().get(1);
    internal_static_response_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_response_descriptor,
        new java.lang.String[] { "Error", "Dealer", "Inning", "ErrDesc", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}
