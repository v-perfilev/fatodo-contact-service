package contracts.relationresource

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    name 'get all common relation with user'
    description 'should return status 200 and list of RelationDTOs'
    request {
        method GET()
        url("/api/relations")
        url($(
                consumer(regex("/api/relations/" + uuid().toString() + "/user")),
                producer("/api/relations/0554562b-1ef5-4311-9462-6aa3c40b5627/user")
        ))
        headers {
            header 'Authorization': $(
                    consumer(containing("Bearer")),
                    producer("Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI4ZjlhN2NhZS03M2M4LTRhZDYtYjEzNS01YmQxMDliNTFkMmUiLCJ1c2VybmFtZSI6InRlc3RfdXNlciIsImF1dGhvcml0aWVzIjoiUk9MRV9VU0VSIiwiaWF0IjowLCJleHAiOjMyNTAzNjc2NDAwfQ.Go0MIqfjREMHOLeqoX2Ej3DbeSG7ZxlL4UAvcxqNeO-RgrKUCrgEu77Ty1vgR_upxVGDAWZS-JfuSYPHSRtv-w")
            )
        }
    }
    response {
        status 200
        headers {
            contentType applicationJson()
        }
        body([
                [
                        id            : anyUuid(),
                        "firstUserId" : anyUuid(),
                        "secondUserId": anyUuid()
                ]
        ])
    }
}
